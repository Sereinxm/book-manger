package com.caoximu.bookmanger.domain.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 借阅记录响应
 *
 * @author caoximu
 */
@Data
@ApiModel(value = "借阅记录响应", description = "借阅记录详细信息")
public class BorrowRecordResponse {

    @ApiModelProperty("借阅记录ID")
    private Long id;

    @ApiModelProperty("借阅用户ID")
    private Long userId;

    @ApiModelProperty("用户姓名")
    private String userName;

    @ApiModelProperty("图书副本ID")
    private Long copyId;

    @ApiModelProperty("图书ISBN")
    private String bookIsbn;

    @ApiModelProperty("图书标题")
    private String bookTitle;

    @ApiModelProperty("作者姓名")
    private String authorName;

    @ApiModelProperty("借阅时间")
    private LocalDateTime borrowDate;

    @ApiModelProperty("应还时间")
    private LocalDateTime dueDate;

    @ApiModelProperty("实际归还时间")
    private LocalDateTime returnDate;

    @ApiModelProperty("借阅状态")
    private String status;

    @ApiModelProperty("续借次数")
    private Integer renewalCount;

    @ApiModelProperty("罚金金额")
    private BigDecimal fineAmount;

    @ApiModelProperty("备注信息")
    private String notes;
} 