package com.caoximu.bookmanger.domain.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author cao32
 */
@Schema(name = "注册登陆响应体")
@Data
public class AuthResponse {
    @Schema(description = "授权令牌")
    private String accessToken;

    @Schema(description = "授权令牌 access_token 的有效期")
    private Long expireIn;
}
