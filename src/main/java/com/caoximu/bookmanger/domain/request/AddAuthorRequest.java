package com.caoximu.bookmanger.domain.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDate;

/**
 * 添加作者请求
 *
 * @author caoximu
 */
@Data
@ApiModel(value = "添加作者请求", description = "添加作者的请求参数")
public class AddAuthorRequest {

    @NotBlank(message = "作者姓名不能为空")
    @Size(max = 100, message = "作者姓名长度不能超过100个字符")
    @ApiModelProperty(value = "作者姓名", required = true)
    private String name;

    @Size(max = 2000, message = "作者简介长度不能超过2000个字符")
    @ApiModelProperty("作者简介")
    private String bio;

    @ApiModelProperty("关联用户ID（可选）")
    private Long userId;

    @ApiModelProperty("出生日期")
    private LocalDate birthDate;

    @Size(max = 100, message = "国籍长度不能超过100个字符")
    @ApiModelProperty("国籍")
    private String nationality;
} 