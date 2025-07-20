package com.caoximu.bookmanger.config;

import com.caoximu.bookmanger.common.R;
import com.caoximu.bookmanger.config.properties.RateLimitProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.util.concurrent.RateLimiter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.Ordered;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

/**
 * 全局限流过滤器
 * 
 * @author cao32
 */
@Slf4j
@Component
@RequiredArgsConstructor
@EnableConfigurationProperties(RateLimitProperties.class)
@ConditionalOnProperty(prefix = "rate-limit", name = "enabled", havingValue = "true", matchIfMissing = true)
public class GlobalRateLimitFilter extends OncePerRequestFilter implements Ordered {

    private final RateLimitProperties rateLimitProperties;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final AntPathMatcher pathMatcher = new AntPathMatcher();
    private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * 全局限流器实例
     */
    private RateLimiter rateLimiter;

    /**
     * 初始化限流器
     */
    @Override
    protected void initFilterBean() throws ServletException {
        super.initFilterBean();
        // 根据配置创建限流器
        this.rateLimiter = RateLimiter.create(rateLimitProperties.getPermitsPerSecond());
        log.info("全局限流过滤器初始化完成，限流速率: {} 请求/秒", rateLimitProperties.getPermitsPerSecond());
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // 检查是否需要跳过限流
        if (shouldSkipRateLimit(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        // 尝试获取令牌
        boolean acquired = rateLimiter.tryAcquire(rateLimitProperties.getTimeoutMillis(), TimeUnit.MILLISECONDS);

        if (!acquired) {
            // 限流触发，返回错误响应
            handleRateLimitExceeded(request, response);
            return;
        }

        // 记录成功获取令牌
        log.debug("请求 {} 成功获取令牌，时间: {}", request.getRequestURI(), LocalDateTime.now().format(dtf));

        // 继续处理请求
        filterChain.doFilter(request, response);
    }

    /**
     * 检查是否需要跳过限流
     * 
     * @param request HTTP请求
     * @return true-跳过限流，false-应用限流
     */
    private boolean shouldSkipRateLimit(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        String[] excludes = rateLimitProperties.getExcludes();

        if (excludes != null && excludes.length > 0) {
            for (String exclude : excludes) {
                if (pathMatcher.match(exclude, requestURI)) {
                    log.debug("请求 {} 匹配排除路径 {}，跳过限流", requestURI, exclude);
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * 处理限流超出的情况
     * 
     * @param request HTTP请求
     * @param response HTTP响应
     */
    private void handleRateLimitExceeded(HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.warn("请求 {} 触发限流，时间: {}, 客户端IP: {}", 
                request.getRequestURI(), 
                LocalDateTime.now().format(dtf),
                getClientIpAddress(request));

        // 设置响应头
        response.setStatus(rateLimitProperties.getErrorCode());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());

        // 构造错误响应
        R<Void> errorResponse = R.fail(rateLimitProperties.getErrorCode(), rateLimitProperties.getErrorMessage());

        // 写入响应
        String jsonResponse = objectMapper.writeValueAsString(errorResponse);
        response.getWriter().write(jsonResponse);
        response.getWriter().flush();
    }

    /**
     * 获取客户端真实IP地址
     * 
     * @param request HTTP请求
     * @return 客户端IP地址
     */
    private String getClientIpAddress(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty() && !"unknown".equalsIgnoreCase(xForwardedFor)) {
            return xForwardedFor.split(",")[0].trim();
        }

        String xRealIp = request.getHeader("X-Real-IP");
        if (xRealIp != null && !xRealIp.isEmpty() && !"unknown".equalsIgnoreCase(xRealIp)) {
            return xRealIp;
        }

        return request.getRemoteAddr();
    }

    @Override
    public int getOrder() {
        // 设置为高优先级，在CORS过滤器之后，认证过滤器之前
        return Ordered.HIGHEST_PRECEDENCE + 1;
    }
} 