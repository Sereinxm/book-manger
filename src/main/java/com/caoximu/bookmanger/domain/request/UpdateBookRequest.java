package com.caoximu.bookmanger.domain.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;

/**
 * 更新图书请求
 *
 * @author caoximu
 */
@Data
@ApiModel(value = "更新图书请求", description = "更新图书信息的请求参数")
public class UpdateBookRequest {

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
} 