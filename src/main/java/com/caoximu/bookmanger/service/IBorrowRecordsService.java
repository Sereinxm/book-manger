package com.caoximu.bookmanger.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.caoximu.bookmanger.domain.request.BorrowBookRequest;
import com.caoximu.bookmanger.domain.request.ReturnBookRequest;
import com.caoximu.bookmanger.domain.response.AuthorBorrowStatisticsResponse;
import com.caoximu.bookmanger.domain.response.BorrowRecordResponse;
import com.caoximu.bookmanger.entity.BorrowRecords;

import java.util.List;

/**
 * <p>
 * 借阅记录表 服务类
 * </p>
 *
 * @author caoximu
 * @since 2025-07-19
 */
public interface IBorrowRecordsService extends IService<BorrowRecords> {

    /**
     * 借阅图书（管理员权限）
     *
     * @param request 借阅请求
     * @return 借阅记录响应
     */
    void borrowBook(BorrowBookRequest request);

    /**
     * 归还图书 （管理员权限）
     *
     * @param request 归还请求
     * @return 借阅记录响应
     */
    void returnBook(ReturnBookRequest request);


    /**
     * 获取所有用户的借阅记录（管理员权限）
     *
     * @param current 当前页
     * @param size 页大小
     * @param userId 用户ID（可选）
     * @param status 借阅状态（可选）
     * @return 分页借阅记录
     */
    Page<BorrowRecordResponse> getBorrowRecords(Long current, Long size, Long userId, String status);

    /**
     * 获取作者图书借阅统计（作者权限）
     *
     * @param authorId 作者ID
     * @return 作者图书借阅统计列表
     */
    List<AuthorBorrowStatisticsResponse> getAuthorBorrowStatistics(Long authorId);

}
