package com.caoximu.bookmanger.domain.dto;

import lombok.Data;

/**
 * Google 用户信息 DTO
 * 
 * @author caoximu
 */
@Data
public class GoogleUserInfoDto {
    
    /**
     * Google 用户ID
     */
    private String id;
    
    /**
     * 用户邮箱
     */
    private String email;
    
    /**
     * 邮箱是否已验证
     */
    private Boolean verified_email;
    
    /**
     * 用户姓名
     */
    private String name;
    
    /**
     * 用户头像URL
     */
    private String picture;
    
    /**
     * 用户语言环境
     */
    private String locale;
} 