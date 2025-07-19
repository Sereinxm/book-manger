package com.caoximu.bookmanger.controller;

import com.caoximu.bookmanger.common.R;
import com.caoximu.bookmanger.common.annotation.RequireRole;
import com.caoximu.bookmanger.domain.response.BookCopyResponse;
import com.caoximu.bookmanger.domain.request.AddBookCopyRequest;
import com.caoximu.bookmanger.domain.request.UpdateBookCopyRequest;
import com.caoximu.bookmanger.entity.enums.BookCopyStatus;
import com.caoximu.bookmanger.entity.enums.UserRole;
import com.caoximu.bookmanger.service.IBookCopiesService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 图书副本表 前端控制器
 * </p>
 *
 * @author caoximu
 * @since 2025-07-19
 */
@Slf4j
@Api(tags = "图书副本管理")
@RestController
@RequestMapping("/bookCopies")
@RequiredArgsConstructor
public class BookCopiesController {

    private final IBookCopiesService bookCopiesService;

    @ApiOperation("为图书添加副本（管理员）")
    @RequireRole(UserRole.ADMIN)
    @PostMapping("/admin/{isbn}/add")
    public R<Void> addCopiesForBook(
            @ApiParam(value = "图书ISBN", required = true) @PathVariable String isbn,
            @RequestBody @Validated AddBookCopyRequest request) {
        log.info("管理员为图书 {} 添加 {} 个副本", isbn, request.getCount());
        bookCopiesService.createCopiesForBook(
                isbn, 
                request.getCount(), 
                request.getLocation(), 
                request.getConditionNotes()
        );
        return R.ok();
    }

    @ApiOperation("更新副本信息（管理员）")
    @RequireRole(UserRole.ADMIN)
    @PutMapping("/admin/{copyId}")
    public R<Void> updateCopy(
            @ApiParam(value = "副本ID", required = true) @PathVariable Long copyId,
            @RequestBody @Validated UpdateBookCopyRequest request) {
        log.info("管理员更新副本 {} 信息", copyId);
        
        // 验证状态是否有效
        BookCopyStatus status = BookCopyStatus.fromCode(request.getStatus());
        if (status == null) {
            return R.fail("无效的副本状态: " + request.getStatus());
        }
        bookCopiesService.updateCopyStatus(copyId, request.getStatus());
        return R.ok();
    }

    @ApiOperation("根据ISBN获取所有副本")
    @GetMapping("/isbn/{isbn}")
    public R<List<BookCopyResponse>> getCopiesByIsbn(
            @ApiParam(value = "图书ISBN", required = true) @PathVariable String isbn) {
        //使用分页查询
        List<BookCopyResponse> result = bookCopiesService.getCopiesByIsbn(isbn);
        return R.ok(result);
    }



}
