package com.caoximu.bookmanger.controller;

import cn.dev33.satoken.oauth2.template.SaOAuth2Util;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.caoximu.bookmanger.common.R;
import com.caoximu.bookmanger.common.annotation.RequireRole;
import com.caoximu.bookmanger.domain.request.AuthorAuthRequest;
import com.caoximu.bookmanger.domain.request.GoogleLoginRequest;
import com.caoximu.bookmanger.domain.request.LoginRequest;
import com.caoximu.bookmanger.domain.request.RegisterRequest;
import com.caoximu.bookmanger.domain.request.UpdateUserProfileRequest;
import com.caoximu.bookmanger.domain.request.UpdateUserRoleRequest;
import com.caoximu.bookmanger.domain.request.UserQueryRequest;
import com.caoximu.bookmanger.domain.response.AuthResponse;
import com.caoximu.bookmanger.domain.response.UserResponse;
import com.caoximu.bookmanger.entity.enums.UserRole;
import com.caoximu.bookmanger.service.IUsersService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 认证控制器
 * 
 * @author caoximu
 */
@Slf4j
@Api(tags = "认证管理")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {


    private final IUsersService usersService;
    @ApiOperation("用户登录")
    @PostMapping("/login")
    public R<AuthResponse> login(@RequestBody @Validated LoginRequest request) {
        return R.ok(usersService.login(request));
    }

    @ApiOperation("用户注册")
    @PostMapping("/register")
    public R<AuthResponse> register(@RequestBody @Validated RegisterRequest request) {
        return R.ok(usersService.register(request));
    }

    @ApiOperation("用户登出")
    @PostMapping("/logout")
    public R<Void> logout() {
        StpUtil.logout();
        return R.ok();
    }

    @ApiOperation("作者认证")
    @RequireRole(UserRole.ADMIN)
    @PostMapping("/authorAuth")
    public R<Void> authorAuth(@RequestBody @Validated AuthorAuthRequest req) {
        usersService.authorAuth(req);
        return R.ok();
    }
    /**
     * 超级管理员查看所有系统用户
     */
    @ApiOperation("查看所有系统用户")
    @RequireRole(UserRole.SUPER_ADMIN)
    @PostMapping("/users")
    public R<IPage<UserResponse>> getUserList(@RequestBody @Validated UserQueryRequest request) {
        return R.ok(usersService.getUserList(request));
    }

    /**
     * 超级管理员对用户权限进行变更
     */
    @ApiOperation("变更用户权限")
    @RequireRole(UserRole.SUPER_ADMIN)
    @PostMapping("/updateUserRole")
    public R<Void> updateUserRole(@RequestBody @Validated UpdateUserRoleRequest request) {
        usersService.updateUserRole(request);
        return R.ok();
    }

    /**
     * 用户修改自己的基本信息
     */
    @ApiOperation("修改个人信息")
    @PostMapping("/updateProfile")
    public R<Void> updateUserProfile(@RequestBody @Validated UpdateUserProfileRequest request) {
        usersService.updateUserProfile(request);
        return R.ok();
    }

    /**
     * 获取Google OAuth2授权URL
     */
    @ApiOperation("获取Google OAuth2授权URL")
    @GetMapping("/google/auth-url")
    public R<String> getGoogleAuthUrl(@RequestParam(required = false) String state) {
        String authUrl = usersService.getGoogleAuthUrl(state);
        return R.ok(authUrl);
    }

    /**
     * Google OAuth2登录
     */
    @ApiOperation("Google OAuth2登录")
    @PostMapping("/google/login")
    public R<AuthResponse> googleLogin(@RequestBody @Validated GoogleLoginRequest request) {
        return R.ok(usersService.googleLogin(request));
    }

}