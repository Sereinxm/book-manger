package com.caoximu.bookmanger.service;

import cn.hutool.core.util.IdUtil;
import cn.hutool.json.JSONObject;
import com.caoximu.bookmanger.config.GoogleOAuth2Config;
import com.caoximu.bookmanger.domain.dto.GoogleUserInfoDto;
import com.caoximu.bookmanger.exception.BizException;
import com.caoximu.bookmanger.utils.HttpUtil;
import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.auth.oauth2.BearerToken;
import com.google.api.client.auth.oauth2.ClientParametersAuthentication;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Google OAuth2 服务类
 * 
 * @author caoximu
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class GoogleOAuth2Service {

    private final GoogleOAuth2Config googleConfig;
    private final HttpUtil httpUtil;

    /**
     * 构建Google OAuth2授权URL
     * 
     * @param state 状态参数，用于防止CSRF攻击
     * @return 授权URL
     */
    public String buildAuthorizationUrl(String state) {
        try {
            StringBuilder url = new StringBuilder();
            url.append(googleConfig.getAuthorizationUri());
            url.append("?response_type=code");
            url.append("&client_id=").append(URLEncoder.encode(googleConfig.getClientId(), StandardCharsets.UTF_8.name()));
            url.append("&redirect_uri=").append(URLEncoder.encode(googleConfig.getRedirectUri(), StandardCharsets.UTF_8.name()));
            url.append("&scope=").append(URLEncoder.encode(googleConfig.getScope(), StandardCharsets.UTF_8.name()));

            if (state != null && !state.trim().isEmpty()) {
                url.append("&state=").append(URLEncoder.encode(state, StandardCharsets.UTF_8.name()));
            }
            
            // 强制显示账号选择页面
            url.append("&prompt=select_account");
            
            String authUrl = url.toString();
            log.info("构建Google授权URL: {}", authUrl);
            
            return authUrl;
            
        } catch (Exception e) {
            log.error("构建Google授权URL失败", e);
            throw new BizException("构建授权URL失败");
        }
    }

    /**
     * 使用授权码交换访问令牌
     * 
     * @param authorizationCode 授权码
     * @return 访问令牌信息
     */
    public JSONObject exchangeCodeForTokens(String authorizationCode) {
        log.info("使用授权码交换Google访问令牌");
        
        try {
            // 创建Google OAuth2流程
            AuthorizationCodeFlow flow = new AuthorizationCodeFlow.Builder(
                    BearerToken.authorizationHeaderAccessMethod(),
                    new NetHttpTransport(),
                    JacksonFactory.getDefaultInstance(),
                    new GenericUrl(googleConfig.getTokenUri()),
                    new ClientParametersAuthentication(googleConfig.getClientId(), googleConfig.getClientSecret()),
                    googleConfig.getClientId(),
                    googleConfig.getAuthorizationUri())
                    .setScopes(Arrays.asList(googleConfig.getScope().split(" ")))
                    .build();
            
            // 使用授权码交换访问令牌
            TokenResponse tokenResponse = flow.newTokenRequest(authorizationCode)
                    .setRedirectUri(googleConfig.getRedirectUri())
                    .execute();
            
            // 将Google的TokenResponse转换为JSONObject格式，保持与原有代码兼容
            JSONObject result = new JSONObject();
            result.put("access_token", tokenResponse.getAccessToken());
            result.put("refresh_token", tokenResponse.getRefreshToken());
            result.put("token_type", tokenResponse.getTokenType());
            result.put("expires_in", tokenResponse.getExpiresInSeconds());
            
            if (tokenResponse.getScope() != null) {
                result.put("scope", tokenResponse.getScope());
            }
            
            log.info("成功获取Google访问令牌");
            return result;
            
        } catch (IOException e) {
            log.error("交换Google访问令牌失败", e);
            throw new BizException("获取Google访问令牌失败: " + e.getMessage());
        } catch (Exception e) {
            log.error("交换Google访问令牌失败", e);
            if (e instanceof BizException) {
                throw e;
            }
            throw new BizException("获取Google访问令牌失败");
        }
    }

    /**
     * 使用访问令牌获取用户信息
     * 
     * @param accessToken 访问令牌
     * @return 用户信息
     */
    public GoogleUserInfoDto getUserInfo(String accessToken) {
        log.info("获取Google用户信息");
        
        try {
            Map<String, String> headers = new HashMap<>();
            headers.put("Authorization", "Bearer " + accessToken);
            
            String response = httpUtil.sendGet(googleConfig.getUserInfoUri(), headers);
            
            JSONObject userInfoJson = httpUtil.parseJsonResponse(response);
            
            // 检查是否有错误
            if (userInfoJson.containsKey("error")) {
                String error = userInfoJson.getStr("error");
                log.error("获取Google用户信息失败: {}", error);
                throw new BizException("获取Google用户信息失败");
            }
            
            // 转换为DTO
            GoogleUserInfoDto userInfo = new GoogleUserInfoDto();
            userInfo.setId(userInfoJson.getStr("id"));
            userInfo.setEmail(userInfoJson.getStr("email"));
            userInfo.setVerified_email(userInfoJson.getBool("verified_email", false));
            userInfo.setName(userInfoJson.getStr("name"));
            userInfo.setPicture(userInfoJson.getStr("picture"));
            userInfo.setLocale(userInfoJson.getStr("locale"));
            
            log.info("成功获取Google用户信息: email={}, verified={}", userInfo.getEmail(), userInfo.getVerified_email());
            
            return userInfo;
            
        } catch (Exception e) {
            log.error("获取Google用户信息失败", e);
            if (e instanceof BizException) {
                throw e;
            }
            throw new BizException("获取Google用户信息失败");
        }
    }

    /**
     * 完整的Google OAuth2登录流程
     * 
     * @param authorizationCode 授权码
     * @return 用户信息
     */
    public GoogleUserInfoDto authenticateWithGoogle(String authorizationCode) {
        log.info("执行Google OAuth2登录流程");
        
        // 1. 使用授权码交换访问令牌
        JSONObject tokenResponse = exchangeCodeForTokens(authorizationCode);
        String accessToken = tokenResponse.getStr("access_token");
        
        if (accessToken == null || accessToken.trim().isEmpty()) {
            throw new BizException("无法获取Google访问令牌");
        }
        
        // 2. 使用访问令牌获取用户信息
        GoogleUserInfoDto userInfo = getUserInfo(accessToken);
        
        // 3. 验证邮箱是否已验证
        if (!Boolean.TRUE.equals(userInfo.getVerified_email())) {
            throw new BizException("Google邮箱未验证，无法登录");
        }
        
        return userInfo;
    }

    /**
     * 生成随机状态参数
     * 
     * @return 随机状态字符串
     */
    public String generateState() {
        return IdUtil.fastSimpleUUID();
    }
} 