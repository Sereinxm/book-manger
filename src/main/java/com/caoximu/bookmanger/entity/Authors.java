package com.caoximu.bookmanger.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <p>
 * 作者表
 * </p>
 *
 * @author caoximu
 * @since 2025-07-19
 */
@Getter
@Setter
@ToString
@Api(tags = "用户管理")
@ApiModel(value = "Authors对象", description = "作者表")
public class Authors implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 作者ID
     */
    @ApiModelProperty("作者ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 作者姓名
     */
    @ApiModelProperty("作者姓名")
    private String name;

    /**
     * 作者简介
     */
    @ApiModelProperty("作者简介")
    private String bio;

    /**
     * 关联用户ID（可选）
     */
    @ApiModelProperty("关联用户ID（可选）")
    private Long userId;

    /**
     * 出生日期
     */
    @ApiModelProperty("出生日期")
    private LocalDate birthDate;

    /**
     * 国籍
     */
    @ApiModelProperty("国籍")
    private String nationality;

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
     * 逻辑删除标志
     */
    @ApiModelProperty("逻辑删除标志")
    private Boolean deleted;

    /**
     * 乐观锁版本号
     */
    @ApiModelProperty("乐观锁版本号")
    private Integer version;
}
