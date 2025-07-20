package com.caoximu.bookmanger.domain.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * Google 登录请求
 * 
 * @author caoximu
 */
@Data
@ApiModel(value = "GoogleLoginRequest", description = "Google登录请求")
public class GoogleLoginRequest {
    
    /**
     * Google OAuth2 授权码
     */
    @ApiModelProperty(value = "Google OAuth2 授权码", required = true)
    @NotBlank(message = "授权码不能为空")
    private String code;
    
    /**
     * 状态参数（可选，用于防止CSRF攻击）
     */
    @ApiModelProperty(value = "状态参数", required = false)
    private String state;
} 