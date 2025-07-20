package com.caoximu.bookmanger.config;

import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpUtil;
import com.caoximu.bookmanger.config.properties.SecurityProperties;
import com.caoximu.bookmanger.exception.BizException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Sa-Token 配置类
 * 
 * @author caoximu
 */
@Slf4j
@Component
@RequiredArgsConstructor
@EnableConfigurationProperties(SecurityProperties.class)
public class SaTokenAuthFilter implements WebMvcConfigurer {

    private final SecurityProperties securityProperties;


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        String[] excludes = securityProperties.getExcludes();

        registry.addInterceptor(new SaInterceptor(handler -> {
                    SaRouter.match("/book").check(() -> {
                        if (!StpUtil.isLogin()) {
                            throw new BizException(HttpStatus.UNAUTHORIZED.value(), "用户未登录");
                        }
                    });
                })).addPathPatterns("/**")
                // 排除不需要拦截的路径
                .excludePathPatterns(excludes);
    }

} 