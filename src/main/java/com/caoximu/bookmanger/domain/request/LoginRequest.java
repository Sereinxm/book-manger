package com.caoximu.bookmanger.domain.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;


/**
 * @author cao32
 */
@Data
public  class LoginRequest {

    @Schema(description = "邮箱",defaultValue = "admin@system.com")
    @NotBlank(message = "登陆账号不能为空")
    private String email;
    @Schema(description = "用户登陆密码" ,defaultValue = "D/ual88ZF0fzFpy3U/sDq7E/tDy2z65imTyVqDpMDsf9L+jEAO03juj1Cj0PZvVHWkm3TwOq9qUTVfBmbIQNeTxNhbgI+F72+RcsxCXaZr97UkoWWlOrC/o747KYz74EslAqToj/xd/MzZDP4GsZmaUxCEC2hGSrnU/OEa3i4X3qToYmLCEAg1Aa5D8tXslP58azssmC7ERHVVD57vM/ANqxL6NZSoxlAOi5X4DtDiRWksXd+41wyd/DDTCW/1zgjeicUbuvpOSq/KEpH0jPcD2sl3zZlQ0tA9tVqC1zpB4WkVZjzBBrJxkCF2XPOjGADg7AWl85R0nmaSAz+n+PuA==")
    private String password;

}