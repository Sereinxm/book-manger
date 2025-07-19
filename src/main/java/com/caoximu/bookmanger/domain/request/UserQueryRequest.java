package com.caoximu.bookmanger.domain.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 用户查询请求
 * 
 * @author caoximu
 */
@Schema(name = "用户查询请求体")
@Data
public class UserQueryRequest {
    
    @Schema(description = "页码", defaultValue = "1")
    private Integer page = 1;
    
    @Schema(description = "每页大小", defaultValue = "10")
    private Integer pageSize = 10;
    
    @Schema(description = "用户姓名(模糊查询)")
    private String name;
    
    @Schema(description = "邮箱地址(模糊查询)")
    private String email;
    
    @Schema(description = "用户角色")
    private String role;
    
    @Schema(description = "账户状态")
    private Boolean isActive;
} 