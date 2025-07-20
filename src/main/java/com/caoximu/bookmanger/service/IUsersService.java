package com.caoximu.bookmanger.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.caoximu.bookmanger.domain.request.AuthorAuthRequest;
import com.caoximu.bookmanger.domain.request.GoogleLoginRequest;
import com.caoximu.bookmanger.domain.request.LoginRequest;
import com.caoximu.bookmanger.domain.request.RegisterRequest;
import com.caoximu.bookmanger.domain.request.UpdateUserProfileRequest;
import com.caoximu.bookmanger.domain.request.UpdateUserRoleRequest;
import com.caoximu.bookmanger.domain.request.UserQueryRequest;
import com.caoximu.bookmanger.domain.response.AuthResponse;
import com.caoximu.bookmanger.domain.response.UserResponse;
import com.caoximu.bookmanger.entity.Users;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author caoximu
 * @since 2025-07-19
 */
public interface IUsersService extends IService<Users> {

    AuthResponse login(LoginRequest request);

    AuthResponse register(RegisterRequest request);

    void authorAuth(AuthorAuthRequest req);

    /**
     * 分页查询用户列表
     */
    IPage<UserResponse> getUserList(UserQueryRequest request);

    /**
     * 更新用户角色和状态
     */
    void updateUserRole(UpdateUserRoleRequest request);

    /**
     * 用户更新个人信息
     */
    void updateUserProfile(UpdateUserProfileRequest request);

    /**
     * 获取Google OAuth2授权URL
     * 
     * @param state 状态参数，用于防止CSRF攻击
     * @return 授权URL
     */
    String getGoogleAuthUrl(String state);

    /**
     * Google OAuth2登录
     * 
     * @param request Google登录请求
     * @return 认证响应
     */
    AuthResponse googleLogin(GoogleLoginRequest request);
}
