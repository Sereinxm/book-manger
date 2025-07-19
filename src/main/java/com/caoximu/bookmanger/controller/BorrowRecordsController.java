package com.caoximu.bookmanger.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.caoximu.bookmanger.common.R;
import com.caoximu.bookmanger.common.annotation.RequireRole;
import com.caoximu.bookmanger.domain.request.BorrowBookRequest;
import com.caoximu.bookmanger.domain.request.ReturnBookRequest;
import com.caoximu.bookmanger.domain.response.AuthorBorrowStatisticsResponse;
import com.caoximu.bookmanger.domain.response.BorrowRecordResponse;
import com.caoximu.bookmanger.entity.Authors;
import com.caoximu.bookmanger.entity.enums.UserRole;
import com.caoximu.bookmanger.exception.BizException;
import com.caoximu.bookmanger.service.IAuthorsService;
import com.caoximu.bookmanger.service.IBorrowRecordsService;
import com.caoximu.bookmanger.utils.LoginHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 借阅记录表 前端控制器
 * </p>
 *
 * @author caoximu
 * @since 2025-07-19
 */
@Slf4j
@Api(tags = "借阅记录管理")
@RestController
@RequestMapping("/borrowRecords")
public class BorrowRecordsController {

    @Resource
    private  IBorrowRecordsService borrowRecordsService;

    @Resource
    private IAuthorsService authorsService;

    @ApiOperation("借阅图书（管理员）")
    @RequireRole(UserRole.ADMIN)
    @PostMapping("/borrow")
    public R<Void> borrowBook(@RequestBody @Validated BorrowBookRequest request) {
        borrowRecordsService.borrowBook(request);
        return R.ok();
    }

    @ApiOperation("归还图书（管理员）")
    @RequireRole(UserRole.ADMIN)
    @PostMapping("/return")
    public R<Void> returnBook(@RequestBody @Validated ReturnBookRequest request) {
        borrowRecordsService.returnBook(request);
        return R.ok();
    }

    @ApiOperation("获取当前用户借阅记录")
    @GetMapping("/my")
    public R<Page<BorrowRecordResponse>> getMyBorrowRecords(
            @ApiParam(value = "当前页", defaultValue = "1") @RequestParam(defaultValue = "1") Long current,
            @ApiParam(value = "页大小", defaultValue = "10") @RequestParam(defaultValue = "10") Long size,
            @ApiParam(value = "借阅状态(active/returned/overdue)") @RequestParam(required = false) String status) {

        Long userId = LoginHelper.getUserId();
        Page<BorrowRecordResponse> result = borrowRecordsService.getBorrowRecords(current, size, userId, status);
        return R.ok(result);
    }

    @ApiOperation("获取所有借阅记录（管理员）")
    @RequireRole(UserRole.ADMIN)
    @GetMapping("/admin/all")
    public R<Page<BorrowRecordResponse>> getAllBorrowRecords(
            @ApiParam(value = "当前页", defaultValue = "1") @RequestParam(defaultValue = "1") Long current,
            @ApiParam(value = "页大小", defaultValue = "10") @RequestParam(defaultValue = "10") Long size,
            @ApiParam(value = "用户ID") @RequestParam(required = false) Long userId,
            @ApiParam(value = "借阅状态(active/returned/overdue)") @RequestParam(required = false) String status) {
        
        Page<BorrowRecordResponse> result = borrowRecordsService.getBorrowRecords(current, size, userId, status);
        return R.ok(result);
    }
    /**
     * 作者可以查看自己图书的借阅总数量
     */
    @ApiOperation("获取作者图书借阅统计（作者）")
    @GetMapping("/author/statistics")
    public R<List<AuthorBorrowStatisticsResponse>> getAuthorBorrowStatistics() {
        // 获取当前登录用户ID
        Long currentUserId = LoginHelper.getUserId();
        
        // 根据用户ID查找对应的作者记录
        LambdaQueryWrapper<Authors> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Authors::getUserId, currentUserId)
                   .eq(Authors::getDeleted, false);
        
        Authors author = authorsService.getOne(queryWrapper);
        if (author == null) {
            throw new BizException("当前用户不是作者或作者信息不存在");
        }
        
        // 获取作者的图书借阅统计
        List<AuthorBorrowStatisticsResponse> statistics = borrowRecordsService.getAuthorBorrowStatistics(author.getId());
        
        return R.ok(statistics);
    }
    
}
