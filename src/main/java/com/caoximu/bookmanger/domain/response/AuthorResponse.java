package com.caoximu.bookmanger.domain.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 作者信息响应
 *
 * @author caoximu
 */
@Data
@ApiModel(value = "作者信息响应", description = "作者详细信息")
public class AuthorResponse {

    @ApiModelProperty("作者ID")
    private Long id;

    @ApiModelProperty("作者姓名")
    private String name;

    @ApiModelProperty("作者简介")
    private String bio;

    @ApiModelProperty("关联用户ID")
    private Long userId;

    @ApiModelProperty("关联用户姓名")
    private String userName;

    @ApiModelProperty("出生日期")
    private LocalDate birthDate;

    @ApiModelProperty("国籍")
    private String nationality;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("更新时间")
    private LocalDateTime updateTime;
} 