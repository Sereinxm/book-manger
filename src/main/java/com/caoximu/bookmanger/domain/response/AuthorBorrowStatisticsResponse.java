package com.caoximu.bookmanger.domain.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

/**
 * 作者借阅统计响应
 *
 * @author caoximu
 * @since 2025-01-27
 */
@Data
@Builder
@ApiModel(value = "作者借阅统计响应", description = "作者图书借阅统计信息")
public class AuthorBorrowStatisticsResponse {

    @ApiModelProperty(value = "图书ISBN")
    private String bookIsbn;

    @ApiModelProperty(value = "图书标题")
    private String bookTitle;

    @ApiModelProperty(value = "总借阅次数")
    private Long totalBorrowCount;

    @ApiModelProperty(value = "当前在借数量")
    private Long currentBorrowCount;

    @ApiModelProperty(value = "已归还数量")
    private Long returnedCount;

    @ApiModelProperty(value = "逾期数量")
    private Long overdueCount;

} 