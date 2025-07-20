package com.caoximu.bookmanger.domain.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 更新系统配置请求
 * 
 * @author caoximu
 */
@Data
@ApiModel(value = "更新系统配置请求", description = "更新系统配置的请求参数")
public class UpdateSystemConfigRequest {

    @ApiModelProperty(value = "配置键", required = true, example = "max_borrow_count")
    @NotBlank(message = "配置键不能为空")
    private String configKey;

    @ApiModelProperty(value = "配置值", required = true, example = "10")
    @NotBlank(message = "配置值不能为空")
    private String configValue;

    @ApiModelProperty(value = "是否启用", example = "true")
    private Boolean isEnabled = true;
} 