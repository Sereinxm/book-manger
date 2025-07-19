package com.caoximu.bookmanger.domain.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 图书副本DTO
 *
 * @author caoximu
 */
@Data
@ApiModel(value = "图书副本DTO", description = "图书副本信息")
public class BookCopyResponse {

    @ApiModelProperty("副本ID")
    private Long copyId;

    @ApiModelProperty("图书ISBN")
    private String bookIsbn;

    @ApiModelProperty("副本状态")
    private String status;

    @ApiModelProperty("存放位置")
    private String location;

    @ApiModelProperty("状态备注")
    private String conditionNotes;

    @ApiModelProperty("是否可借阅")
    private Boolean available;
} 