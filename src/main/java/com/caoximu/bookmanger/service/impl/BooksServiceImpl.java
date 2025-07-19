package com.caoximu.bookmanger.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.caoximu.bookmanger.domain.response.BookCopyResponse;
import com.caoximu.bookmanger.domain.response.BookResponse;
import com.caoximu.bookmanger.domain.request.AddBookRequest;
import com.caoximu.bookmanger.domain.request.BookQueryRequest;
import com.caoximu.bookmanger.domain.request.UpdateBookRequest;
import com.caoximu.bookmanger.entity.Authors;
import com.caoximu.bookmanger.entity.Books;
import com.caoximu.bookmanger.exception.BizException;
import com.caoximu.bookmanger.mapper.BooksMapper;
import com.caoximu.bookmanger.service.IAuthorsService;
import com.caoximu.bookmanger.service.IBookCopiesService;
import com.caoximu.bookmanger.service.IBooksService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 图书表 服务实现类
 * </p>
 *
 * @author caoximu
 * @since 2025-07-19
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class BooksServiceImpl extends ServiceImpl<BooksMapper, Books> implements IBooksService {

    private final IBookCopiesService bookCopiesService;
    private final IAuthorsService authorsService;
    private final BooksMapper booksMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addBook(AddBookRequest request) {
        log.info("开始添加图书，ISBN: {}", request.getIsbn());
        
        // 1. 校验ISBN唯一性
        if (existsByIsbn(request.getIsbn())) {
            throw new BizException("ISBN已存在: " + request.getIsbn());
        }
        
        // 2. 校验作者是否存在
        Authors author = authorsService.getById(request.getAuthorId());
        if (author == null || Boolean.TRUE.equals(author.getDeleted())) {
            throw new BizException("作者不存在，ID: " + request.getAuthorId());
        }
        
        // 3. 创建图书记录
        Books book = new Books();
        book.setIsbn(request.getIsbn());
        book.setTitle(request.getTitle());
        book.setPublisher(request.getPublisher());
        book.setPublishYear(request.getPublishYear());
        book.setDescription(request.getDescription());
        book.setAuthorId(request.getAuthorId());
        
        save(book);
        
        // 4. 创建图书副本
        bookCopiesService.createCopiesForBook(
            request.getIsbn(),
            request.getInitialCopies(),
            request.getLocation(),
            request.getDescription()
        );
        
        log.info("成功添加图书，ISBN: {}，副本数量: {}", request.getIsbn(), request.getInitialCopies());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateBook(String isbn, UpdateBookRequest request) {
        log.info("开始更新图书，ISBN: {}", isbn);
        
        // 1. 检查图书是否存在
        Books existingBook = getById(isbn);
        if (existingBook == null || Boolean.TRUE.equals(existingBook.getDeleted())) {
            throw new BizException("图书不存在，ISBN: " + isbn);
        }
        
        // 3. 校验作者是否存在
        Authors author = authorsService.getById(request.getAuthorId());
        if (author == null || Boolean.TRUE.equals(author.getDeleted())) {
            throw new BizException("作者不存在，ID: " + request.getAuthorId());
        }
        
        // 4. 更新图书信息
        LambdaUpdateWrapper<Books> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Books::getIsbn, isbn)
                    .set(Books::getTitle, request.getTitle())
                    .set(Books::getPublisher, request.getPublisher())
                    .set(Books::getPublishYear, request.getPublishYear())
                    .set(Books::getDescription, request.getDescription())
                    .set(Books::getAuthorId, request.getAuthorId());
                    
        boolean updated = update(updateWrapper);
        if (!updated) {
            throw new BizException("更新失败");
        }
        
        log.info("成功更新图书，ISBN: {}", isbn);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteBook(String isbn) {
        log.info("开始删除图书，ISBN: {}", isbn);
        
        // 1. 检查图书是否存在
        Books book = getById(isbn);
        if (book == null || Boolean.TRUE.equals(book.getDeleted())) {
            throw new BizException("图书不存在，ISBN: " + isbn);
        }
        
        // 2. 检查是否可以删除（没有借出的副本）
        if (!canDelete(isbn)) {
            throw new BizException("图书有副本正在借出中，无法删除，ISBN: " + isbn);
        }
        
        // 3. 删除所有副本（逻辑删除）
        bookCopiesService.deleteAllCopiesByIsbn(isbn);
        
        // 4. 删除图书记录（逻辑删除）
        LambdaUpdateWrapper<Books> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Books::getIsbn, isbn)
                    .set(Books::getDeleted, true)
                    .set(Books::getUpdateTime, LocalDateTime.now());
                    
        update(updateWrapper);
        
        log.info("成功删除图书，ISBN: {}", isbn);
    }

    @Override
    public BookResponse getBookByIsbn(String isbn, boolean includeCopies) {
        Books book = getById(isbn);
        if (book == null || Boolean.TRUE.equals(book.getDeleted())) {
            return null;
        }

        BookResponse resp = new BookResponse();
        resp.setIsbn(book.getIsbn());
        resp.setTitle(book.getTitle());
        resp.setPublisher(book.getPublisher());
        resp.setPublishYear(book.getPublishYear());
        resp.setDescription(book.getDescription());
        resp.setAuthorId(book.getAuthorId());

        // 获取作者信息
        Authors author = authorsService.getById(book.getAuthorId());
        if (author != null && !Boolean.TRUE.equals(author.getDeleted())) {
            resp.setAuthorName(author.getName());
        }

        // 如果需要包含副本详细信息
        if (includeCopies) {
            List<BookCopyResponse> copies = bookCopiesService.getCopiesByIsbn(book.getIsbn());
            resp.setCopies(copies);
        }

        return resp;
    }

    @Override
    public Page<BookResponse> getBooks(BookQueryRequest request) {
        Page<BookResponse> page = new Page<>(request.getCurrent(), request.getSize());
        return booksMapper.selectBooksWithAuthor(page, request);
    }



    private boolean existsByIsbn(String isbn) {
        LambdaQueryWrapper<Books> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Books::getIsbn, isbn)
                .eq(Books::getDeleted, false);
        return count(queryWrapper) > 0;
    }


    private boolean canDelete(String isbn) {
        return !bookCopiesService.hasBorrowedCopies(isbn);
    }

}
