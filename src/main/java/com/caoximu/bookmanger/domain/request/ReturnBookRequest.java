package com.caoximu.bookmanger.domain.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * 归还图书请求
 *
 * @author caoximu
 */
@Data
@ApiModel(value = "归还图书请求", description = "用户归还图书")
public class ReturnBookRequest {

    @NotNull(message = "借阅记录ID不能为空")
    @ApiModelProperty(value = "借阅记录ID", required = true)
    private Long borrowRecordId;

    @Size(max = 500, message = "备注信息长度不能超过500个字符")
    @ApiModelProperty("归还备注")
    private String notes;
} 