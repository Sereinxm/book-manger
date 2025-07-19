package com.caoximu.bookmanger.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 借阅记录表
 * </p>
 *
 * @author caoximu
 * @since 2025-07-19
 */
@Getter
@Setter
@ToString
@TableName("borrow_records")
@ApiModel(value = "BorrowRecords对象", description = "借阅记录表")
public class BorrowRecords implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 借阅记录ID
     */
    @ApiModelProperty("借阅记录ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 借阅用户ID
     */
    @ApiModelProperty("借阅用户ID")
    private Long userId;

    /**
     * 图书副本ID
     */
    @ApiModelProperty("图书副本ID")
    private Long copyId;

    /**
     * 借阅时间
     */
    @ApiModelProperty("借阅时间")
    private LocalDateTime borrowDate;

    /**
     * 应还时间
     */
    @ApiModelProperty("应还时间")
    private LocalDateTime dueDate;

    /**
     * 实际归还时间
     */
    @ApiModelProperty("实际归还时间")
    private LocalDateTime returnDate;

    /**
     * 借阅状态 (active, returned, overdue)
     */
    @ApiModelProperty("借阅状态 (active, returned, overdue)")
    private String status;

    /**
     * 续借次数
     */
    @ApiModelProperty("续借次数")
    private Integer renewalCount;

    /**
     * 罚金金额
     */
    @ApiModelProperty("罚金金额")
    private BigDecimal fineAmount;

    /**
     * 备注信息
     */
    @ApiModelProperty("备注信息")
    private String notes;

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

}
