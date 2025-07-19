package com.caoximu.bookmanger.service;

import com.caoximu.bookmanger.domain.dto.GoogleUserInfoDto;
import com.caoximu.bookmanger.domain.response.AuthResponse;

/**
 * Google OAuth2 认证服务接口
 * 
 * @author caoximu
 */
public interface IGoogleOAuth2Service {
    
    /**
     * 获取 Google OAuth2 授权链接
     * 
     * @param state 状态参数，用于防止CSRF攻击
     * @return 授权链接
     */
    String getAuthorizationUrl(String state);
    
    /**
     * 通过授权码获取访问令牌
     * 
     * @param code 授权码
     * @return 访问令牌
     */
    String getAccessToken(String code);
    
    /**
     * 通过访问令牌获取用户信息
     * 
     * @param accessToken 访问令牌
     * @return Google用户信息
     */
    GoogleUserInfoDto getUserInfo(String accessToken);
    
    /**
     * Google 登录处理
     * 
     * @param code 授权码
     * @return 认证响应
     */
    AuthResponse login(String code);
} 