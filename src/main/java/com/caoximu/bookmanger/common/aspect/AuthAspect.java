package com.caoximu.bookmanger.common.aspect;

import cn.dev33.satoken.stp.StpUtil;
import com.caoximu.bookmanger.common.annotation.RequireRole;
import com.caoximu.bookmanger.entity.Users;
import com.caoximu.bookmanger.entity.enums.UserRole;
import com.caoximu.bookmanger.exception.BizException;
import com.caoximu.bookmanger.mapper.UsersMapper;
import com.caoximu.bookmanger.utils.LoginHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * 权限检查切面
 * 
 * @author caoximu
 */
@Slf4j
@Aspect
@Component
@Order(1)
@RequiredArgsConstructor
public class AuthAspect {

    private final UsersMapper usersMapper;

    /**
     * 方法级别的权限检查
     */
    @Before("@annotation(requireRole)")
    public void checkMethodAuth(JoinPoint joinPoint, RequireRole requireRole) {
        checkUserPermission(requireRole);
    }

    /**
     * 类级别的权限检查
     */
    @Before("@within(requireRole) && !@annotation(com.caoximu.bookmanger.common.annotation.RequireRole)")
    public void checkClassAuth(JoinPoint joinPoint, RequireRole requireRole) {
        checkUserPermission(requireRole);
    }

    /**
     * 检查用户权限
     */
    private void checkUserPermission(RequireRole requireRole) {
        // 1. 检查是否登录
        if (!StpUtil.isLogin()) {
            throw new BizException("请先登录");
        }

        // 2. 获取当前用户ID
        Long userId = LoginHelper.getUserId();
        if (Objects.isNull(userId)) {
            throw new BizException("用户信息异常，请重新登录");
        }

        // 3. 查询用户信息
        Users user = usersMapper.selectById(userId);
        if (Objects.isNull(user)) {
            throw new BizException("用户不存在");
        }

        // 4. 检查用户状态
        if (!user.getIsActive()) {
            throw new BizException("用户已被冻结");
        }

        // 5. 检查用户角色权限
        UserRole userRole = UserRole.fromCode(user.getRole());
        UserRole requiredRole = requireRole.value();
        
        if (!userRole.hasPermissionLevel(requiredRole)) {
            log.warn("用户权限不足，用户角色: {}, 需要角色: {}, 用户ID: {}", 
                     userRole.getDescription(), requiredRole.getDescription(), userId);
            throw new BizException(requireRole.message());
        }

        log.debug("权限检查通过，用户ID: {}, 用户角色: {}, 需要角色: {}", 
                  userId, userRole.getDescription(), requiredRole.getDescription());
    }
}