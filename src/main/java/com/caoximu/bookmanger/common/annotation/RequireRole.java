package com.caoximu.bookmanger.common.annotation;

import com.caoximu.bookmanger.entity.enums.UserRole;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 角色权限注解
 * 用于Controller方法或类上，标注需要的最低角色权限
 * 
 * @author caoximu
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequireRole {
    
    /**
     * 需要的最低角色权限
     * 默认为普通用户
     */
    UserRole value() default UserRole.USER;
    
    /**
     * 权限描述信息
     */
    String message() default "权限不足";
}