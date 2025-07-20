package com.caoximu.bookmanger.domain.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 更新图书副本请求
 *
 * @author caoximu
 */
@Data
@ApiModel(value = "更新图书副本请求", description = "更新图书副本信息")
public class UpdateBookCopyRequest {

    @NotBlank(message = "副本状态不能为空")
    @ApiModelProperty(value = "副本状态", required = true)
    private String status;

    @Size(max = 200, message = "存放位置长度不能超过200个字符")
    @ApiModelProperty("存放位置")
    private String location;

    @Size(max = 1000, message = "状态备注长度不能超过1000个字符")
    @ApiModelProperty("状态备注")
    private String conditionNotes;

    @NotNull(message = "版本号不能为空")
    @ApiModelProperty(value = "版本号（用于乐观锁）", required = true)
    private Integer version;
} 