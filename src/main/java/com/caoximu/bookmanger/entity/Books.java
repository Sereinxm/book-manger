package com.caoximu.bookmanger.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.Version;
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
 * 图书表
 * </p>
 *
 * @author caoximu
 * @since 2025-07-19
 */
@Getter
@Setter
@ToString
@ApiModel(value = "Books对象", description = "图书表")
public class Books implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ISBN码（国际标准书号）
     */
    @TableId("isbn")
    @ApiModelProperty("ISBN码（国际标准书号）")
    private String isbn;

    /**
     * 书名
     */
    @ApiModelProperty("书名")
    private String title;

    /**
     * 出版社
     */
    @ApiModelProperty("出版社")
    private String publisher;

    /**
     * 出版年份
     */
    @ApiModelProperty("出版年份")
    private LocalDate publishYear;

    /**
     * 图书描述
     */
    @ApiModelProperty("图书描述")
    private String description;

    /**
     * 作者ID
     */
    @ApiModelProperty("作者ID")
    private Long authorId;

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

}
