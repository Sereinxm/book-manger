package com.caoximu.bookmanger.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.caoximu.bookmanger.domain.request.AddAuthorRequest;
import com.caoximu.bookmanger.domain.request.AuthorQueryRequest;
import com.caoximu.bookmanger.domain.request.UpdateAuthorRequest;
import com.caoximu.bookmanger.domain.response.AuthorResponse;
import com.caoximu.bookmanger.entity.Authors;

/**
 * <p>
 * 作者表 服务类
 * </p>
 *
 * @author caoximu
 * @since 2025-07-19
 */
public interface IAuthorsService extends IService<Authors> {

    /**
     * 添加作者
     *
     * @param request 添加作者请求
     */
    void addAuthor(AddAuthorRequest request);

    /**
     * 更新作者信息
     *
     * @param authorId 作者ID
     * @param request  更新作者请求
     */
    void updateAuthor(Long authorId, UpdateAuthorRequest request);

    /**
     * 删除作者
     *
     * @param authorId 作者ID
     */
    void deleteAuthor(Long authorId);

    /**
     * 根据ID获取作者详情
     *
     * @param authorId 作者ID
     * @return 作者详情
     */
    AuthorResponse getAuthorById(Long authorId);

    /**
     * 分页查询作者
     *
     * @param request 查询请求
     * @return 分页结果
     */
    Page<AuthorResponse> getAuthors(AuthorQueryRequest request);
}
