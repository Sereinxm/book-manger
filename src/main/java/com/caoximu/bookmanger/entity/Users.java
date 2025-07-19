package com.caoximu.bookmanger.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 用户表
 * </p>
 *
 * @author caoximu
 * @since 2025-07-19
 */
@Getter
@Setter
@ToString
@ApiModel(value = "Users对象", description = "用户表")
public class Users implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    @ApiModelProperty("用户ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户姓名
     */
    @ApiModelProperty("用户姓名")
    private String name;

    /**
     * 邮箱地址
     */
    @ApiModelProperty("邮箱地址")
    private String email;

    /**
     * Google OAuth ID
     */
    @ApiModelProperty("Google OAuth ID")
    private String googleId;

    /**
     * 密码
     */
    @ApiModelProperty("密码")
    private String password;

    /**
     * 用户角色 (admin, user, super_admin)
     */
    @ApiModelProperty("用户角色 (admin, user, super_admin)")
    private String role;

    /**
     * 注册时间
     */
    @ApiModelProperty("注册时间")
    private LocalDateTime joinDate;

    /**
     * 账户状态（1:活跃 0:冻结）
     */
    @ApiModelProperty("账户状态（1:活跃 0:冻结）")
    private Boolean isActive;

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
