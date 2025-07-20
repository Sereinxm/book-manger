package com.caoximu.bookmanger.domain.request;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Data;

/**
 * 更新用户权限请求
 * 
 * @author caoximu
 */
@Schema(name = "更新用户权限请求体")
@Data
public class UpdateUserRoleRequest {
    
    @Schema(description = "用户ID")
    @NotNull(message = "用户ID不能为空")
    private Long userId;
    
    @Schema(description = "新角色")
    @NotNull(message = "角色不能为空")
    private String role;
    
    @Schema(description = "是否激活账户")
    private Boolean isActive;
} 