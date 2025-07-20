package com.caoximu.bookmanger.domain.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 注册请求
 * @author cao32
 */
@Data
public class RegisterRequest {
    @NotBlank(message = "名称不能为空")
    private String name;
    @NotBlank(message = "邮箱不能为空")
    private String email;
    @NotBlank(message = "密码不能为空")
    private String password;
}