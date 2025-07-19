package com.caoximu.bookmanger.domain.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;

/**
 * 添加图书请求
 *
 * @author caoximu
 */
@Data
@ApiModel(value = "添加图书请求", description = "添加图书的请求参数")
public class AddBookRequest {

    @NotBlank(message = "ISBN不能为空")
    @Size(max = 20, message = "ISBN长度不能超过20个字符")
    @ApiModelProperty(value = "ISBN码（国际标准书号）", required = true)
    private String isbn;

    @NotBlank(message = "书名不能为空")
    @Size(max = 500, message = "书名长度不能超过500个字符")
    @ApiModelProperty(value = "书名", required = true)
    private String title;

    @NotBlank(message = "出版社不能为空")
    @Size(max = 200, message = "出版社名称长度不能超过200个字符")
    @ApiModelProperty(value = "出版社", required = true)
    private String publisher;

    @NotNull(message = "出版年份不能为空")
    @ApiModelProperty(value = "出版年份", required = true)
    private LocalDate publishYear;

    @Size(max = 2000, message = "图书描述长度不能超过2000个字符")
    @ApiModelProperty("图书描述")
    private String description;

    @NotNull(message = "作者ID不能为空")
    @ApiModelProperty(value = "作者ID", required = true)
    private Long authorId;

    @NotNull(message = "副本数量不能为空")
    @Min(value = 1, message = "副本数量至少为1")
    @ApiModelProperty(value = "初始副本数量", required = true)
    private Integer initialCopies;

    @ApiModelProperty("副本存放位置")
    private String location;
} 