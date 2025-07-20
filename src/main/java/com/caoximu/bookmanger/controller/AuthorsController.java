package com.caoximu.bookmanger.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.caoximu.bookmanger.common.R;
import com.caoximu.bookmanger.common.annotation.RequireRole;
import com.caoximu.bookmanger.domain.request.AddAuthorRequest;
import com.caoximu.bookmanger.domain.request.AuthorQueryRequest;
import com.caoximu.bookmanger.domain.request.UpdateAuthorRequest;
import com.caoximu.bookmanger.domain.response.AuthorResponse;
import com.caoximu.bookmanger.entity.enums.UserRole;
import com.caoximu.bookmanger.service.IAuthorsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 作者表 前端控制器
 * </p>
 *
 * @author caoximu
 * @since 2025-07-19
 */
@Slf4j
@Api(tags = "作者管理")
@RestController
@RequestMapping("/authors")
@RequiredArgsConstructor
public class AuthorsController {

    private final IAuthorsService authorsService;

    @ApiOperation("添加作者（管理员）")
    @RequireRole(UserRole.ADMIN)
    @PostMapping("/admin/add")
    public R<Void> addAuthor(@RequestBody @Validated AddAuthorRequest request) {
        log.info("管理员添加作者请求，姓名：{}", request.getName());
        authorsService.addAuthor(request);
        return R.ok();
    }

    @ApiOperation("更新作者信息（管理员）")
    @RequireRole(UserRole.ADMIN)
    @PutMapping("/admin/{authorId}")
    public R<Void> updateAuthor(
            @ApiParam(value = "作者ID", required = true) @PathVariable Long authorId,
            @RequestBody @Validated UpdateAuthorRequest request) {
        log.info("管理员更新作者请求，ID：{}", authorId);
        authorsService.updateAuthor(authorId, request);
        return R.ok();
    }

    @ApiOperation("删除作者（管理员）")
    @RequireRole(UserRole.ADMIN)
    @DeleteMapping("/admin/{authorId}")
    public R<Void> deleteAuthor(
            @ApiParam(value = "作者ID", required = true) @PathVariable Long authorId) {
        log.info("管理员删除作者请求，ID：{}", authorId);
        authorsService.deleteAuthor(authorId);
        return R.ok();
    }

    @ApiOperation("根据ID获取作者详情")
    @GetMapping("/{authorId}")
    public R<AuthorResponse> getAuthorById(
            @ApiParam(value = "作者ID", required = true) @PathVariable Long authorId) {
        log.info("查询作者详情，ID：{}", authorId);
        AuthorResponse result = authorsService.getAuthorById(authorId);
        return R.ok(result);
    }

    @ApiOperation("分页查询作者")
    @GetMapping("/search")
    public R<Page<AuthorResponse>> searchAuthors(AuthorQueryRequest request) {
        log.info("分页查询作者，查询参数：{}", request);
        Page<AuthorResponse> result = authorsService.getAuthors(request);
        return R.ok(result);
    }

    @ApiOperation("获取所有作者列表")
    @GetMapping("/list")
    public R<Page<AuthorResponse>> getAuthorList(
            @ApiParam(value = "当前页", defaultValue = "1") @RequestParam(defaultValue = "1") Long current,
            @ApiParam(value = "页大小", defaultValue = "50") @RequestParam(defaultValue = "50") Long size,
            @ApiParam(value = "作者姓名（模糊查询）") @RequestParam(required = false) String name) {
        
        AuthorQueryRequest request = new AuthorQueryRequest();
        request.setCurrent(current);
        request.setSize(size);
        request.setName(name);
        
        Page<AuthorResponse> result = authorsService.getAuthors(request);
        return R.ok(result);
    }
}
