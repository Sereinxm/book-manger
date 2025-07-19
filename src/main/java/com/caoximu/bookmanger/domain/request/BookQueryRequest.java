package com.caoximu.bookmanger.domain.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 图书查询请求
 *
 * @author caoximu
 */
@Data
@ApiModel(value = "图书查询请求", description = "图书查询的请求参数")
public class BookQueryRequest {

    @ApiModelProperty("ISBN（精确匹配）")
    private String isbn;

    @ApiModelProperty("书名（模糊匹配）")
    private String title;

    @ApiModelProperty("出版社（模糊匹配）")
    private String publisher;

    @ApiModelProperty("作者姓名（模糊匹配）")
    private String authorName;

    @ApiModelProperty("页码（从1开始）")
    private Long current = 1L;

    @ApiModelProperty("每页大小")
    private Long size = 10L;
} 