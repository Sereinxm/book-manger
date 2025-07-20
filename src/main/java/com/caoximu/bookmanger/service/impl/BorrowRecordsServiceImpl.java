package com.caoximu.bookmanger.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.caoximu.bookmanger.domain.request.BorrowBookRequest;
import com.caoximu.bookmanger.domain.request.ReturnBookRequest;
import com.caoximu.bookmanger.domain.response.AuthorBorrowStatisticsResponse;
import com.caoximu.bookmanger.domain.response.BorrowRecordResponse;
import com.caoximu.bookmanger.entity.*;
import com.caoximu.bookmanger.entity.enums.BookCopyStatus;
import com.caoximu.bookmanger.entity.enums.BorrowStatus;
import com.caoximu.bookmanger.entity.enums.SystemConfigType;
import com.caoximu.bookmanger.exception.BizException;
import com.caoximu.bookmanger.mapper.BooksMapper;
import com.caoximu.bookmanger.mapper.BorrowRecordsMapper;
import com.caoximu.bookmanger.service.*;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 借阅记录表 服务实现类
 * </p>
 *
 * @author caoximu
 * @since 2025-07-19
 */
@Slf4j
@Service
public class BorrowRecordsServiceImpl extends ServiceImpl<BorrowRecordsMapper, BorrowRecords> implements IBorrowRecordsService {

    @Resource
    private IBookCopiesService bookCopiesService;
    @Resource
    private IBooksService booksService;
    @Resource
    private IUsersService usersService;
    @Resource
    private BorrowRecordsMapper borrowRecordsMapper;

    @Resource
    private ISystemConfigsService systemConfigsService;
    @Resource
    private BooksMapper booksMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void borrowBook(BorrowBookRequest request) {
        log.info("用户借阅图书，ISBN: {}", request.getIsbn());

        Long userId = request.getUserId();
        // 2. 检查用户借阅量是否达上限
        BigDecimal maxBorrowCount = (BigDecimal) systemConfigsService.getSystemConfig(SystemConfigType.MAX_BORROW_COUNT);

        int currentBorrowCount = borrowRecordsMapper.getActiveBorrowCount(userId);
        if (currentBorrowCount >= maxBorrowCount.intValue()) {
            throw new BizException("您已达到最大借阅量，当前借阅：" + currentBorrowCount + "本，最大允许：" + maxBorrowCount + "本");
        }

        // 3. 查询图书是否存在
        Books book = booksMapper.getByIsbn(request.getIsbn());
        if (book == null || Boolean.TRUE.equals(book.getDeleted())) {
            throw new BizException("图书不存在或已删除，ISBN: " + request.getIsbn());
        }

        // 4. 查询是否有可用副本
        BookCopies availableCopy = getAvailableCopy(request.getIsbn());
        if (availableCopy == null) {
            throw new BizException("该图书暂无可借阅副本");
        }

        // 5. 创建借阅记录
        LocalDateTime now = LocalDateTime.now();
        BigDecimal borrowDurationDays = (BigDecimal) systemConfigsService.getSystemConfig(SystemConfigType.BORROW_DURATION_DAYS);
        LocalDateTime dueDate = now.plusDays(borrowDurationDays.intValue());

        BorrowRecords borrowRecord = new BorrowRecords();
        borrowRecord.setUserId(userId);
        borrowRecord.setCopyId(availableCopy.getCopyId());
        borrowRecord.setBorrowDate(now);
        borrowRecord.setDueDate(dueDate);
        borrowRecord.setStatus(BorrowStatus.ACTIVE.getCode());
        borrowRecord.setRenewalCount(++currentBorrowCount);
        borrowRecord.setFineAmount(BigDecimal.ZERO);
        borrowRecord.setNotes(request.getNotes());
        save(borrowRecord);

        // 7. 更新副本状态为已借出
        bookCopiesService.updateCopyStatus(availableCopy.getCopyId(), BookCopyStatus.BORROWED.getCode());

        log.info("借阅成功，用户ID: {}, 副本ID: {}, 应还日期: {}", userId, availableCopy.getCopyId(), dueDate);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void returnBook(ReturnBookRequest request) {
        log.info("归还图书，借阅记录ID: {}", request.getBorrowRecordId());

        // 2. 查询借阅记录
        BorrowRecords borrowRecord = getById(request.getBorrowRecordId());
        if (borrowRecord == null) {
            throw new BizException("借阅记录不存在");
        }

        // 4. 检查是否已经归还
        if (BorrowStatus.isReturned(borrowRecord.getStatus())) {
            throw new BizException("该图书已经归还");
        }

        // 5. 计算是否逾期和罚金
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime dueDate = borrowRecord.getDueDate();
        BigDecimal fineAmount = BigDecimal.ZERO;
        
        if (now.isAfter(dueDate)) {
            // 计算逾期天数
            long overdueDays = ChronoUnit.DAYS.between(dueDate, now);
            BigDecimal finePerDay =(BigDecimal) systemConfigsService.getSystemConfig(SystemConfigType.OVERDUE_FINE_PER_DAY);
            fineAmount = finePerDay.multiply(BigDecimal.valueOf(overdueDays));
            log.info("图书逾期归还，逾期天数: {}, 罚金: {}", overdueDays, fineAmount);
        }

        // 6. 更新借阅记录
        borrowRecord.setReturnDate(now);
        borrowRecord.setStatus(BorrowStatus.RETURNED.getCode());
        borrowRecord.setFineAmount(fineAmount);
        if (StringUtils.hasText(request.getNotes())) {
            borrowRecord.setNotes(borrowRecord.getNotes() + " | 归还备注: " + request.getNotes());
        }

        updateById(borrowRecord);

        // 7. 更新副本状态为可借阅
        bookCopiesService.updateCopyStatus(borrowRecord.getCopyId(), BookCopyStatus.AVAILABLE.getCode());

        log.info("归还成功，借阅记录ID: {}, 罚金: {}", request.getBorrowRecordId(), fineAmount);
    }


    @Override
    public Page<BorrowRecordResponse> getBorrowRecords(Long current, Long size, Long userId, String status) {
        Page<BorrowRecords> page = new Page<>(current, size);
        return borrowRecordsMapper.getBorrowRecords(page, userId, status);
    }

    @Override
    public List<AuthorBorrowStatisticsResponse> getAuthorBorrowStatistics(Long authorId) {
        log.info("获取作者借阅统计，作者ID: {}", authorId);
        List<AuthorBorrowStatisticsResponse> statistics = borrowRecordsMapper.getAuthorBorrowStatistics(authorId);
        log.info("作者 {} 共有 {} 本图书有借阅记录", authorId, statistics.size());
        return statistics;
    }

    /**
     * 获取可用副本
     */
    private BookCopies getAvailableCopy(String isbn) {
        LambdaQueryWrapper<BookCopies> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(BookCopies::getBookIsbn, isbn)
                   .eq(BookCopies::getStatus, BookCopyStatus.AVAILABLE.getCode())
                   .eq(BookCopies::getDeleted, false)
                   .last("LIMIT 1");
        
        return bookCopiesService.getOne(queryWrapper);
    }


}
