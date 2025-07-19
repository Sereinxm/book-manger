package com.caoximu.bookmanger.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.caoximu.bookmanger.config.GoogleOAuth2Config;
import com.caoximu.bookmanger.domain.dto.GoogleUserInfoDto;
import com.caoximu.bookmanger.domain.response.AuthResponse;
import com.caoximu.bookmanger.entity.Users;
import com.caoximu.bookmanger.entity.enums.UserRole;
import com.caoximu.bookmanger.exception.BizException;
import com.caoximu.bookmanger.mapper.UsersMapper;
import com.caoximu.bookmanger.service.IGoogleOAuth2Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Google OAuth2 认证服务实现类
 * 
 * @author caoximu
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class GoogleOAuth2ServiceImpl implements IGoogleOAuth2Service {
    
    private final GoogleOAuth2Config googleOAuth2Config;
    private final UsersMapper usersMapper;
    
    @Override
    public String getAuthorizationUrl(String state) {
        StringBuilder urlBuilder = new StringBuilder();
        urlBuilder.append(googleOAuth2Config.getAuthorizationUri())
                .append("?client_id=").append(googleOAuth2Config.getClientId())
                .append("&redirect_uri=").append(googleOAuth2Config.getRedirectUri())
                .append("&scope=").append(googleOAuth2Config.getScope())
                .append("&response_type=code")
                .append("&access_type=offline");
        
        if (StrUtil.isNotBlank(state)) {
            urlBuilder.append("&state=").append(state);
        }
        
        return urlBuilder.toString();
    }
    
    @Override
    public String getAccessToken(String code) {
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("client_id", googleOAuth2Config.getClientId());
            params.put("client_secret", googleOAuth2Config.getClientSecret());
            params.put("code", code);
            params.put("grant_type", "authorization_code");
            params.put("redirect_uri", googleOAuth2Config.getRedirectUri());
            
            HttpResponse response = HttpRequest.post(googleOAuth2Config.getTokenUri())
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .form(params)
                    .execute();
            
            if (!response.isOk()) {
                log.error("获取访问令牌失败，响应状态码: {}, 响应内容: {}", response.getStatus(), response.body());
                throw new BizException("获取Google访问令牌失败");
            }
            
            JSONObject jsonObject = JSONUtil.parseObj(response.body());
            String accessToken = jsonObject.getStr("access_token");
            
            if (StrUtil.isBlank(accessToken)) {
                log.error("访问令牌为空，响应内容: {}", response.body());
                throw new BizException("获取Google访问令牌失败");
            }
            
            return accessToken;
        } catch (Exception e) {
            log.error("获取Google访问令牌异常", e);
            throw new BizException("获取Google访问令牌失败: " + e.getMessage());
        }
    }
    
    @Override
    public GoogleUserInfoDto getUserInfo(String accessToken) {
        try {
            HttpResponse response = HttpRequest.get(googleOAuth2Config.getUserInfoUri())
                    .header("Authorization", "Bearer " + accessToken)
                    .execute();
            
            if (!response.isOk()) {
                log.error("获取用户信息失败，响应状态码: {}, 响应内容: {}", response.getStatus(), response.body());
                throw new BizException("获取Google用户信息失败");
            }
            
            return JSONUtil.toBean(response.body(), GoogleUserInfoDto.class);
        } catch (Exception e) {
            log.error("获取Google用户信息异常", e);
            throw new BizException("获取Google用户信息失败: " + e.getMessage());
        }
    }
    
    @Override
    public AuthResponse login(String code) {
        // 1. 获取访问令牌
        String accessToken = getAccessToken(code);
        
        // 2. 获取用户信息
        GoogleUserInfoDto googleUserInfo = getUserInfo(accessToken);
        
        // 3. 验证邮箱
        if (!googleUserInfo.getVerified_email()) {
            throw new BizException("Google邮箱未验证，无法登录");
        }
        
        // 4. 查找或创建用户
        Users user = findOrCreateUser(googleUserInfo);
        
        // 5. 执行登录
        StpUtil.login(user.getId());
        
        // 6. 返回认证信息
        AuthResponse authResponse = new AuthResponse();
        authResponse.setAccessToken(StpUtil.getTokenName());
        authResponse.setExpireIn(StpUtil.getTokenTimeout());
        
        return authResponse;
    }
    
    /**
     * 查找或创建用户
     * 
     * @param googleUserInfo Google用户信息
     * @return 用户实体
     */
    private Users findOrCreateUser(GoogleUserInfoDto googleUserInfo) {
        // 先根据Google ID查找用户
        LambdaQueryWrapper<Users> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Users::getGoogleId, googleUserInfo.getId());
        Users user = usersMapper.selectOne(wrapper);
        
        if (Objects.nonNull(user)) {
            // 检查账号状态
            if (!user.getIsActive()) {
                throw new BizException("账号已被冻结，请联系管理员");
            }
            // 更新用户信息
            updateUserInfo(user, googleUserInfo);
            return user;
        }
        
        // 根据邮箱查找现有用户
        wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Users::getEmail, googleUserInfo.getEmail());
        user = usersMapper.selectOne(wrapper);
        
        if (Objects.nonNull(user)) {
            // 检查账号状态
            if (!user.getIsActive()) {
                throw new BizException("账号已被冻结，请联系管理员");
            }
            // 绑定Google ID
            user.setGoogleId(googleUserInfo.getId());
            user.setName(googleUserInfo.getName());
            user.setUpdateTime(LocalDateTime.now());
            usersMapper.updateById(user);
            return user;
        }
        
        // 创建新用户
        return createNewUser(googleUserInfo);
    }
    
    /**
     * 更新用户信息
     */
    private void updateUserInfo(Users user, GoogleUserInfoDto googleUserInfo) {
        user.setName(googleUserInfo.getName());
        user.setEmail(googleUserInfo.getEmail());
        user.setUpdateTime(LocalDateTime.now());
        usersMapper.updateById(user);
    }
    
    /**
     * 创建新用户
     */
    private Users createNewUser(GoogleUserInfoDto googleUserInfo) {
        Users user = new Users();
        user.setGoogleId(googleUserInfo.getId());
        user.setName(googleUserInfo.getName());
        user.setEmail(googleUserInfo.getEmail());
        user.setRole(UserRole.USER.getValue());
        user.setIsActive(true);
        user.setJoinDate(LocalDateTime.now());
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());
        // Google 登录用户不需要密码和盐值
        user.setPassword(null);
        user.setSalt(null);
        
        usersMapper.insert(user);
        return user;
    }
} 