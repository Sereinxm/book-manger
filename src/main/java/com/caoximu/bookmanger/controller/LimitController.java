package com.caoximu.bookmanger.controller;

import com.caoximu.bookmanger.common.R;
import com.caoximu.bookmanger.config.properties.RateLimitProperties;
import com.google.common.util.concurrent.RateLimiter;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 限流测试控制器
 * 
 * @author cao32
 */
@Slf4j
@RestController
@RequestMapping("/limit")
@Api(tags = "限流控制器")
@RequiredArgsConstructor
public class LimitController {
    
    /**
     * 局部限流策略：1秒钟2个请求（用于对比）
     */
    private final RateLimiter localLimiter = RateLimiter.create(2.0);
    
    private final RateLimitProperties rateLimitProperties;
    private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @ApiOperation("测试局部限流")
    @GetMapping("/test-local")
    public R<String> testLocalLimiter() {
        // 500毫秒内，没拿到令牌，就直接进入服务降级
        boolean tryAcquire = localLimiter.tryAcquire(500, TimeUnit.MILLISECONDS);

        if (!tryAcquire) {
            log.warn("局部限流触发，时间: {}", LocalDateTime.now().format(dtf));
            return R.fail(429, "局部限流触发，请稍后再试");
        }

        log.info("局部限流 - 获取令牌成功，时间: {}", LocalDateTime.now().format(dtf));
        return R.ok("局部限流测试通过，当前时间：" + LocalDateTime.now().format(dtf));
    }

    /**
     * 测试全局限流效果
     */
    @ApiOperation("测试全局限流效果")
    @GetMapping("/test-global")
    public R<String> testGlobalLimiter() {
        // 这个接口会被全局限流过滤器拦截
        log.info("全局限流测试接口被调用，时间: {}", LocalDateTime.now().format(dtf));
        return R.ok("全局限流测试通过，当前时间：" + LocalDateTime.now().format(dtf));
    }

    /**
     * 获取当前限流配置信息
     */
    @ApiOperation("获取限流配置信息")
    @GetMapping("/config")
    public R<Map<String, Object>> getRateLimitConfig() {
        Map<String, Object> config = new HashMap<>();
        config.put("enabled", rateLimitProperties.isEnabled());
        config.put("permitsPerSecond", rateLimitProperties.getPermitsPerSecond());
        config.put("timeoutMillis", rateLimitProperties.getTimeoutMillis());
        config.put("errorCode", rateLimitProperties.getErrorCode());
        config.put("errorMessage", rateLimitProperties.getErrorMessage());
        config.put("excludes", rateLimitProperties.getExcludes());
        config.put("currentTime", LocalDateTime.now().format(dtf));
        
        return R.ok(config);
    }

    /**
     * 压力测试接口
     */
    @ApiOperation("压力测试接口")
    @GetMapping("/stress-test")
    public R<String> stressTest() {
        log.info("压力测试接口被调用，时间: {}", LocalDateTime.now().format(dtf));
        return R.ok("压力测试接口响应，时间：" + LocalDateTime.now().format(dtf));
    }
}

