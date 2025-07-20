package com.caoximu.bookmanger.entity.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * 用户角色枚举
 * 
 * @author caoximu
 */
public enum UserRole {
    
    /**
     * 普通用户
     */
    USER("user", "普通用户"),
    
    /**
     * 管理员
     */
    ADMIN("admin", "管理员"),
    
    /**
     * 超级管理员
     */
    SUPER_ADMIN("super_admin", "超级管理员");
    
    @EnumValue
    private final String code;
    private final String description;
    
    UserRole(String code, String description) {
        this.code = code;
        this.description = description;
    }
    
    public String getCode() {
        return code;
    }
    
    public String getDescription() {
        return description;
    }
    
    /**
     * 根据代码获取角色
     */
    public static UserRole fromCode(String code) {
        for (UserRole role : values()) {
            if (role.getCode().equals(code)) {
                return role;
            }
        }
        throw new IllegalArgumentException("未知的用户角色代码: " + code);
    }
    
    /**
     * 获取角色权限级别（数值越大权限越高）
     */
    public int getLevel() {
        if (this == USER){
            return 1;
        } else if (this == ADMIN) {
            return 2;
        } else if (this == SUPER_ADMIN){
            return 3;
        }
        return 0;
    }
    
    /**
     * 判断是否为管理员角色
     */
    public boolean isAdmin() {
        return this == ADMIN || this == SUPER_ADMIN;
    }
    
    /**
     * 判断是否为超级管理员
     */
    public boolean isSuperAdmin() {
        return this == SUPER_ADMIN;
    }
    
    /**
     * 判断当前角色权限是否高于或等于指定角色
     */
    public boolean hasPermissionLevel(UserRole targetRole) {
        return this.getLevel() >= targetRole.getLevel();
    }
} 