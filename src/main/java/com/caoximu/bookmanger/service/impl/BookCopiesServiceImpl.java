package com.caoximu.bookmanger.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.caoximu.bookmanger.domain.response.BookCopyResponse;
import com.caoximu.bookmanger.entity.BookCopies;
import com.caoximu.bookmanger.entity.enums.BookCopyStatus;
import com.caoximu.bookmanger.mapper.BookCopiesMapper;
import com.caoximu.bookmanger.service.IBookCopiesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 图书副本表 服务实现类
 * </p>
 *
 * @author caoximu
 * @since 2025-07-19
 */
@Slf4j
@Service
public class BookCopiesServiceImpl extends ServiceImpl<BookCopiesMapper, BookCopies> implements IBookCopiesService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createCopiesForBook(String isbn, int count, String location, String conditionNotes) {
        log.info("为图书 {} 创建 {} 个副本", isbn, count);
        
        List<BookCopies> copies = new ArrayList<>();
        
        for (int i = 0; i < count; i++) {
            BookCopies copy = new BookCopies();
            copy.setBookIsbn(isbn);
            copy.setStatus(BookCopyStatus.AVAILABLE.getCode());
            copy.setLocation(StringUtils.hasText(location) ? location : "默认位置");
            copy.setConditionNotes(conditionNotes);
            copies.add(copy);
        }
        // 批量保存
        saveBatch(copies);
        
        log.info("成功为图书 {} 创建了 {} 个副本", isbn, count);
    }

    @Override
    public List<BookCopyResponse> getCopiesByIsbn(String isbn) {
        LambdaQueryWrapper<BookCopies> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(BookCopies::getBookIsbn, isbn)
                   .eq(BookCopies::getDeleted, false)
                   .orderByDesc(BookCopies::getCreateTime);
                   
        List<BookCopies> copies = list(queryWrapper);
        return convertToDtos(copies);
    }

    @Override
    public boolean hasBorrowedCopies(String isbn) {
        LambdaQueryWrapper<BookCopies> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(BookCopies::getBookIsbn, isbn)
                   .eq(BookCopies::getStatus, BookCopyStatus.BORROWED.getCode())
                   .eq(BookCopies::getDeleted, false);
                   
        return count(queryWrapper) > 0;
    }


    @Override
    public void deleteAllCopiesByIsbn(String isbn) {
        log.info("删除图书 {} 的所有副本", isbn);
        
        LambdaUpdateWrapper<BookCopies> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(BookCopies::getBookIsbn, isbn)
                    .set(BookCopies::getDeleted, true)
                    .set(BookCopies::getUpdateTime, LocalDateTime.now());
                    
        update(updateWrapper);
        log.info("成功删除图书 {} 的所有副本", isbn);
    }

    @Override
    public void updateCopyStatus(Long copyId, String status) {
        log.info("更新副本 {} 状态为 {}", copyId, status);
        
        LambdaUpdateWrapper<BookCopies> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(BookCopies::getCopyId, copyId)
                    .set(BookCopies::getStatus, status)
                    .set(BookCopies::getUpdateTime, LocalDateTime.now());
                    
        update(updateWrapper);
    }

    @Override
    public BookCopyResponse convertToDto(BookCopies bookCopy) {
        if (bookCopy == null) {
            return null;
        }
        
        BookCopyResponse dto = new BookCopyResponse();
        dto.setCopyId(bookCopy.getCopyId());
        dto.setBookIsbn(bookCopy.getBookIsbn());
        dto.setStatus(bookCopy.getStatus());
        dto.setLocation(bookCopy.getLocation());
        dto.setConditionNotes(bookCopy.getConditionNotes());
        dto.setAvailable(BookCopyStatus.isAvailable(bookCopy.getStatus()));
        
        return dto;
    }

    @Override
    public List<BookCopyResponse> convertToDtos(List<BookCopies> bookCopies) {
        if (bookCopies == null || bookCopies.isEmpty()) {
            return new ArrayList<>();
        }
        
        return bookCopies.stream()
                        .map(this::convertToDto)
                        .collect(Collectors.toList());
    }
}
