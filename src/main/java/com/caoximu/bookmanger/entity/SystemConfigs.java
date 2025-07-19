package com.caoximu.bookmanger.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 系统配置表
 * </p>
 *
 * @author caoximu
 * @since 2025-07-19
 */
@Getter
@Setter
@ToString
@TableName("system_configs")
@ApiModel(value = "SystemConfigs对象", description = "系统配置表")
public class SystemConfigs implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 配置ID
     */
    @ApiModelProperty("配置ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 配置键
     */
    @ApiModelProperty("配置键")
    private String configKey;

    /**
     * 配置值
     */
    @ApiModelProperty("配置值")
    private String configValue;

    /**
     * 配置类型 (string, number, boolean)
     */
    @ApiModelProperty("配置类型 (string, number, boolean)")
    private String configType;

    /**
     * 配置描述
     */
    @ApiModelProperty("配置描述")
    private String description;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @ApiModelProperty("更新时间")
    private LocalDateTime updateTime;

    /**
     * 是否开启（1:开启 0:关闭）
     */
    @ApiModelProperty("是否开启（1:开启 0:关闭）")
    private Boolean isEnabled;
}
