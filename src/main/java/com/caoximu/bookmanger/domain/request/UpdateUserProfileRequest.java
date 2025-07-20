package com.caoximu.bookmanger.domain.request;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Data;

/**
 * 用户更新个人信息请求
 * 
 * @author caoximu
 */
@Schema(name = "用户更新个人信息请求体")
@Data
public class UpdateUserProfileRequest {
    
    @Schema(description = "用户姓名")
    @NotBlank(message = "用户姓名不能为空")
    private String name;
    
    @Schema(description = "新密码(可选)")
    private String newPassword;
    
    @Schema(description = "旧密码(修改密码时必填)")
    private String oldPassword;
} 