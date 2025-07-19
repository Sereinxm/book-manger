package com.caoximu.bookmanger.domain.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户信息响应
 * 
 * @author caoximu
 */
@Schema(name = "用户信息响应体")
@Data
public class UserResponse {
    
    @Schema(description = "用户ID")
    private Long id;
    
    @Schema(description = "用户姓名")
    private String name;
    
    @Schema(description = "邮箱地址")
    private String email;
    
    @Schema(description = "用户角色")
    private String role;
    
    @Schema(description = "注册时间")
    private LocalDateTime joinDate;
    
    @Schema(description = "账户状态")
    private Boolean isActive;
    
    @Schema(description = "创建时间")
    private LocalDateTime createTime;
    
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
} 