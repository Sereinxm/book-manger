package com.caoximu.bookmanger.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Google OAuth2 配置类
 * 
 * @author caoximu
 */
@Data
@Component
@ConfigurationProperties(prefix = "google.oauth2")
public class GoogleOAuth2Config {
    
    /**
     * Google OAuth2 客户端ID
     */
    private String clientId;
    
    /**
     * Google OAuth2 客户端密钥
     */
    private String clientSecret;
    
    /**
     * 重定向URI
     */
    private String redirectUri;
    
    /**
     * Google OAuth2 授权端点
     */
    private String authorizationUri = "https://accounts.google.com/o/oauth2/auth";
    
    /**
     * Google OAuth2 令牌端点
     */
    private String tokenUri = "https://oauth2.googleapis.com/token";
    
    /**
     * Google 用户信息端点
     */
    private String userInfoUri = "https://www.googleapis.com/oauth2/v2/userinfo";
    
    /**
     * OAuth2 作用域
     */
    private String scope = "openid email profile";
} 