package com.caoximu.bookmanger.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 图书副本表
 * </p>
 *
 * @author caoximu
 * @since 2025-07-19
 */
@Getter
@Setter
@ToString
@TableName("book_copies")
@ApiModel(value = "BookCopies对象", description = "图书副本表")
public class BookCopies implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 副本ID
     */
    @ApiModelProperty("副本ID")
    @TableId(value = "copy_id", type = IdType.AUTO)
    private Long copyId;

    /**
     * 图书ISBN
     */
    @ApiModelProperty("图书ISBN")
    private String bookIsbn;

    /**
     * 副本状态
     */
    @ApiModelProperty("副本状态")
    private String status;

    /**
     * 存放位置
     */
    @ApiModelProperty("存放位置")
    private String location;

    /**
     * 状态备注
     */
    @ApiModelProperty("状态备注")
    private String conditionNotes;

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
