package com.caoximu.bookmanger.domain.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;


/**
 * @author cao32
 */
@Data
public  class LoginRequest {

    @Schema(description = "登陆账号，手机，邮箱")
    @NotBlank(message = "登陆账号不能为空")
    private String email;
    @Schema(description = "用户登陆密码")
    private String password;

}