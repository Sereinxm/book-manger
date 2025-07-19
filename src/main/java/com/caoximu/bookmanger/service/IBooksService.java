package com.caoximu.bookmanger.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.caoximu.bookmanger.domain.response.BookResponse;
import com.caoximu.bookmanger.domain.request.AddBookRequest;
import com.caoximu.bookmanger.domain.request.BookQueryRequest;
import com.caoximu.bookmanger.domain.request.UpdateBookRequest;
import com.caoximu.bookmanger.entity.Books;

/**
 * <p>
 * 图书表 服务类
 * </p>
 *
 * @author caoximu
 * @since 2025-07-19
 */
public interface IBooksService extends IService<Books> {

    /**
     * 添加图书（管理员功能）
     * 1. 校验ISBN唯一性
     * 2. 校验作者是否存在
     * 3. 创建图书记录
     * 4. 自动创建指定数量的副本，状态为available
     *
     * @param request 添加图书请求
     * @param userId
     * @return 图书详细信息
     */
    void addBook(AddBookRequest request);

    /**
     * 更新图书信息（管理员功能）
     * 注意：不允许修改ISBN
     *
     * @param isbn 图书ISBN
     * @param request 更新请求
     * @return 更新后的图书信息
     */
    void updateBook(String isbn, UpdateBookRequest request);

    /**
     * 删除图书（管理员功能）
     * 1. 检查所有副本状态，只有当所有副本都不是borrowed状态时才能删除
     * 2. 级联删除所有副本记录
     * 3. 删除图书记录（逻辑删除）
     *
     * @param isbn 图书ISBN
     */
    void deleteBook(String isbn);

    /**
     * 根据ISBN获取图书详细信息
     *
     * @param isbn 图书ISBN
     * @param includeCopies 是否包含副本信息
     * @return 图书详细信息
     */
    BookResponse getBookByIsbn(String isbn, boolean includeCopies);

    /**
     * 分页查询图书
     *
     * @param request 查询请求
     * @return 分页结果
     */
    Page<BookResponse> getBooks(BookQueryRequest request);

}
