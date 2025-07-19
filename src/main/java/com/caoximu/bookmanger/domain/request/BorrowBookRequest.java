package com.caoximu.bookmanger.domain.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * 借阅图书请求
 *
 * @author caoximu
 */
@Data
@ApiModel(value = "借阅图书请求", description = "用户借阅图书")
public class BorrowBookRequest {

    @NotBlank(message = "图书ISBN不能为空")
    @ApiModelProperty(value = "图书ISBN", required = true)
    private String isbn;

    @NotNull
    @ApiModelProperty(value = "用户id", required = true)
    private Long userId;

    @Size(max = 500, message = "备注信息长度不能超过500个字符")
    @ApiModelProperty("备注信息")
    private String notes;
} 