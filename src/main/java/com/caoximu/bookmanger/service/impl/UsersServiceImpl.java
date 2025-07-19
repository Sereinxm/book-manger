package com.caoximu.bookmanger.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.caoximu.bookmanger.config.properties.CustomProperties;
import com.caoximu.bookmanger.domain.request.AuthorAuthRequest;
import com.caoximu.bookmanger.domain.request.LoginRequest;
import com.caoximu.bookmanger.domain.request.RegisterRequest;
import com.caoximu.bookmanger.domain.request.UpdateUserProfileRequest;
import com.caoximu.bookmanger.domain.request.UpdateUserRoleRequest;
import com.caoximu.bookmanger.domain.request.UserQueryRequest;
import com.caoximu.bookmanger.domain.response.AuthResponse;
import com.caoximu.bookmanger.domain.response.UserResponse;
import com.caoximu.bookmanger.entity.Authors;
import com.caoximu.bookmanger.entity.Users;
import com.caoximu.bookmanger.entity.enums.UserRole;
import com.caoximu.bookmanger.exception.BizException;
import com.caoximu.bookmanger.mapper.AuthorsMapper;
import com.caoximu.bookmanger.mapper.UsersMapper;
import com.caoximu.bookmanger.service.IUsersService;
import com.caoximu.bookmanger.utils.LoginHelper;
import com.caoximu.bookmanger.utils.PasswordUtil;
import com.caoximu.bookmanger.utils.SignatureUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author caoximu
 * @since 2025-07-19
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class UsersServiceImpl extends ServiceImpl<UsersMapper, Users> implements IUsersService {

    private final UsersMapper usersMapper;
    private final AuthorsMapper authorsMapper;
    private final CustomProperties customProperties;
    
    @Override
    public AuthResponse login(LoginRequest request) {
        Users appUser = usersMapper.getAppUser(request.getEmail());
        checkAppUser(appUser);
        String decrypt = SignatureUtil.decryptWithRsa(request.getPassword(), customProperties.getSignatureKey().getPrivateKey());
        if (!PasswordUtil.matches(appUser.getSalt() , decrypt, appUser.getPassword())) {
            throw new BizException("密码错误");
        }
        StpUtil.login(appUser.getId());
        AuthResponse resp = new AuthResponse();
        resp.setAccessToken(StpUtil.getTokenName());
        resp.setExpireIn(StpUtil.getTokenTimeout());
        return resp;
    }

    @Override
    public AuthResponse register(RegisterRequest request) {
        Users users = usersMapper.getAppUser(request.getEmail());
        //如果邮箱已存在并且账号可用
        if (Objects.nonNull(users)){
            if (users.getIsActive()){
                throw new BizException("用户已存在");
            }
            else {
                throw new BizException("账号已存在并处于冻结状态，请先解冻");
            }
        }
        //构建用户
        String decrypt = SignatureUtil.decryptWithRsa(request.getPassword(), customProperties.getSignatureKey().getPrivateKey());
        Users user = new Users();
        user.setName(request.getName());
        String salt = IdUtil.fastSimpleUUID();
        String password = PasswordUtil.encryptPwd(salt, decrypt);
        user.setSalt(salt);
        user.setEmail(request.getEmail());
        user.setPassword(password);
        this.save(user);
        //进行用户登录
        StpUtil.login(user.getId());
        AuthResponse resp = new AuthResponse();
        resp.setAccessToken(StpUtil.getTokenName());
        resp.setExpireIn(StpUtil.getTokenTimeout());
        return resp;
    }

    @Override
    public void authorAuth(AuthorAuthRequest req) {
        Long userId1 = req.getUserId();
        Users users1 = usersMapper.selectById(userId1);
        checkAppUser(users1);
        Long authorId = req.getAuthorId();
        Authors authors = authorsMapper.selectById(authorId);
        if (Objects.isNull(authors)) {
            throw new BizException("作者不存在,请先添加作者");
        }
        //关联用户
        authors.setUserId(userId1);
        authorsMapper.updateById(authors);
    }

    @Override
    public IPage<UserResponse> getUserList(UserQueryRequest request) {
        LambdaQueryWrapper<Users> queryWrapper = new LambdaQueryWrapper<>();
        
        // 条件查询
        queryWrapper.like(StrUtil.isNotBlank(request.getName()), Users::getName, request.getName())
                   .like(StrUtil.isNotBlank(request.getEmail()), Users::getEmail, request.getEmail())
                   .eq(StrUtil.isNotBlank(request.getRole()), Users::getRole, request.getRole())
                   .eq(Objects.nonNull(request.getIsActive()), Users::getIsActive, request.getIsActive())
                   .orderByDesc(Users::getCreateTime);
        
        // 分页查询
        Page<Users> page = new Page<>(request.getPage(), request.getPageSize());
        IPage<Users> userPage = this.page(page, queryWrapper);
        
        // 转换为响应对象
        return userPage.convert(this::convertToUserResponse);
    }

    @Override
    public void updateUserRole(UpdateUserRoleRequest request) {
        Users user = this.getById(request.getUserId());
        if (Objects.isNull(user)) {
            throw new BizException("用户不存在");
        }
        
        // 验证角色合法性
        try {
            UserRole.fromCode(request.getRole());
        } catch (IllegalArgumentException e) {
            throw new BizException("无效的用户角色");
        }
        
        // 更新用户信息
        user.setRole(request.getRole());
        if (Objects.nonNull(request.getIsActive())) {
            user.setIsActive(request.getIsActive());
        }
        
        this.updateById(user);
    }

    @Override
    public void updateUserProfile(UpdateUserProfileRequest request) {
        Long currentUserId = LoginHelper.getUserId();
        Users user = this.getById(currentUserId);
        if (Objects.isNull(user)) {
            throw new BizException("用户不存在");
        }

        
        // 更新基本信息
        user.setName(request.getName());
        
        // 处理密码更新
        if (StrUtil.isNotBlank(request.getNewPassword())) {
            if (StrUtil.isBlank(request.getOldPassword())) {
                throw new BizException("修改密码时必须提供旧密码");
            }
            
            // 验证旧密码
            String decryptOldPwd = SignatureUtil.decryptWithRsa(request.getOldPassword(), customProperties.getSignatureKey().getPrivateKey());
            if (!PasswordUtil.matches(user.getSalt(), decryptOldPwd, user.getPassword())) {
                throw new BizException("旧密码不正确");
            }
            
            // 设置新密码
            String decryptNewPwd = SignatureUtil.decryptWithRsa(request.getNewPassword(), customProperties.getSignatureKey().getPrivateKey());
            String newSalt = IdUtil.fastSimpleUUID();
            String newPassword = PasswordUtil.encryptPwd(newSalt, decryptNewPwd);
            user.setSalt(newSalt);
            user.setPassword(newPassword);
        }
        
        this.updateById(user);
    }

    private void checkAppUser(Users appUser) {
        if (Objects.isNull(appUser)) {
            throw new BizException("用户不存在");
        }

        if (!appUser.getIsActive()) {
            throw new BizException("用户已冻结");
        }
    }
    
    private UserResponse convertToUserResponse(Users user) {
        UserResponse response = new UserResponse();
        BeanUtils.copyProperties(user, response);
        return response;
    }
}
