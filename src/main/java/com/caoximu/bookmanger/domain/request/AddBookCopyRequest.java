package com.caoximu.bookmanger.domain.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 添加图书副本请求
 *
 * @author caoximu
 */
@Data
@ApiModel(value = "添加图书副本请求", description = "为已存在的图书添加副本")
public class AddBookCopyRequest {

    @NotNull(message = "副本数量不能为空")
    @Min(value = 1, message = "副本数量至少为1")
    @ApiModelProperty(value = "副本数量", required = true)
    private Integer count;

    @Size(max = 200, message = "存放位置长度不能超过200个字符")
    @ApiModelProperty("存放位置")
    private String location;

    @Size(max = 1000, message = "状态备注长度不能超过1000个字符")
    @ApiModelProperty("状态备注")
    private String conditionNotes;
} 