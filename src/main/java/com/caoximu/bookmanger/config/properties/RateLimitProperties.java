package com.caoximu.bookmanger.config.properties;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 限流配置属性
 * 
 * @author cao32
 */
@Data
@NoArgsConstructor
@Configuration
@ConfigurationProperties(prefix = "rate-limit")
public class RateLimitProperties {

    /**
     * 是否启用限流
     */
    private boolean enabled = true;

    /**
     * 每秒允许的请求数量
     */
    private double permitsPerSecond = 2.0;

    /**
     * 获取令牌的超时时间（毫秒）
     */
    private long timeoutMillis = 500;

    /**
     * 排除限流的路径
     */
    private String[] excludes = {};

    /**
     * 限流失败时的错误码
     */
    private int errorCode = 429;

    /**
     * 限流失败时的错误消息
     */
    private String errorMessage = "请求过于频繁，请稍后再试";
} 