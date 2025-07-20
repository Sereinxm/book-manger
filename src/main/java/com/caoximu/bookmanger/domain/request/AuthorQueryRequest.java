package com.caoximu.bookmanger.domain.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 作者查询请求
 *
 * @author caoximu
 */
@Data
@ApiModel(value = "作者查询请求", description = "作者查询的请求参数")
public class AuthorQueryRequest {

    @ApiModelProperty("作者姓名（模糊匹配）")
    private String name;

    @ApiModelProperty("国籍（精确匹配）")
    private String nationality;

    @ApiModelProperty("关联用户ID")
    private Long userId;

    @ApiModelProperty("页码（从1开始）")
    private Long current = 1L;

    @ApiModelProperty("每页大小")
    private Long size = 10L;
}