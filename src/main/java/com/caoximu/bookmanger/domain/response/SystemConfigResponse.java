package com.caoximu.bookmanger.domain.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 系统配置响应
 * 
 * @author caoximu
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "系统配置响应", description = "系统配置信息")
public class SystemConfigResponse {

    @ApiModelProperty("配置ID")
    private Long id;

    @ApiModelProperty("配置键")
    private String configKey;

    @ApiModelProperty("配置值")
    private String configValue;

    @ApiModelProperty("配置类型")
    private String configType;

    @ApiModelProperty("配置描述")
    private String description;

    @ApiModelProperty("是否开启")
    private Boolean isEnabled;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("更新时间")
    private LocalDateTime updateTime;
} 