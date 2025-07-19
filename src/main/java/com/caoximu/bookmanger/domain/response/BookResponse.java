package com.caoximu.bookmanger.domain.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

/**
 * 图书信息DTO
 *
 * @author caoximu
 */
@Data
@ApiModel(value = "图书信息DTO", description = "图书详细信息")
public class BookResponse {

    @ApiModelProperty("ISBN码（国际标准书号）")
    private String isbn;

    @ApiModelProperty("书名")
    private String title;

    @ApiModelProperty("出版社")
    private String publisher;

    @ApiModelProperty("出版年份")
    private LocalDate publishYear;

    @ApiModelProperty("图书描述")
    private String description;

    @ApiModelProperty("作者ID")
    private Long authorId;

    @ApiModelProperty("作者姓名")
    private String authorName;

    @ApiModelProperty("副本列表")
    private List<BookCopyResponse> copies;
} 