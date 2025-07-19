package com.caoximu.bookmanger.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.caoximu.bookmanger.common.R;
import com.caoximu.bookmanger.common.annotation.RequireRole;
import com.caoximu.bookmanger.domain.response.BookResponse;
import com.caoximu.bookmanger.domain.request.AddBookRequest;
import com.caoximu.bookmanger.domain.request.BookQueryRequest;
import com.caoximu.bookmanger.domain.request.UpdateBookRequest;
import com.caoximu.bookmanger.entity.enums.UserRole;
import com.caoximu.bookmanger.service.IBooksService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 图书表 前端控制器
 * </p>
 *
 * @author caoximu
 * @since 2025-07-19
 */
@Slf4j
@Api(tags = "图书管理")
@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
public class BooksController {

    private final IBooksService booksService;

    @ApiOperation("添加图书（管理员）")
    @RequireRole(UserRole.ADMIN)
    @PostMapping("/admin/add")
    public R<Void> addBook(@RequestBody @Validated AddBookRequest request) {
        booksService.addBook(request);
        return R.ok();
    }

    @ApiOperation("更新图书信息（管理员）")
    @RequireRole(UserRole.ADMIN)
    @PutMapping("/admin/{isbn}")
    public R<Void> updateBook(
            @ApiParam(value = "图书ISBN", required = true) @PathVariable String isbn,
            @RequestBody @Validated UpdateBookRequest request) {
        log.info("管理员更新图书请求，ISBN: {}", isbn);
        booksService.updateBook(isbn, request);
        return R.ok();
    }

    @ApiOperation("删除图书（管理员）")
    @RequireRole(UserRole.ADMIN)
    @DeleteMapping("/admin/{isbn}")
    public R<Void> deleteBook(
            @ApiParam(value = "图书ISBN", required = true) @PathVariable String isbn) {
        log.info("管理员删除图书请求，ISBN: {}", isbn);
        booksService.deleteBook(isbn);
        return R.ok();
    }

    @ApiOperation("根据ISBN获取图书详情")
    @GetMapping("/{isbn}")
    public R<BookResponse> getBookByIsbn(
            @ApiParam(value = "图书ISBN", required = true) @PathVariable String isbn,
            @ApiParam(value = "是否包含副本信息", defaultValue = "true") @RequestParam(defaultValue = "true") Boolean includeCopies) {
        BookResponse result = booksService.getBookByIsbn(isbn, includeCopies);
        return R.ok(result);
    }

    @ApiOperation("分页查询图书")
    @GetMapping("/search")
    public R<Page<BookResponse>> searchBooks(BookQueryRequest request) {
        Page<BookResponse> result = booksService.getBooks(request);
        return R.ok(result);
    }

}
