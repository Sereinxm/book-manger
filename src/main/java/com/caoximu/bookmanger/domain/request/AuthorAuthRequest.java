package com.caoximu.bookmanger.domain.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Schema(name = "作者认证请求体")
@Data
public class AuthorAuthRequest {
    @NotBlank(message = "作者ID不能为空")
    private Long authorId;
    @NotBlank(message = "用户不能为空")
    private Long userId;
}
