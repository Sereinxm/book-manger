package com.caoximu.bookmanger.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.caoximu.bookmanger.domain.response.BookResponse;
import com.caoximu.bookmanger.domain.request.BookQueryRequest;
import com.caoximu.bookmanger.entity.Books;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 图书表 Mapper 接口
 * </p>
 *
 * @author caoximu
 * @since 2025-07-19
 */
@Mapper
public interface BooksMapper extends BaseMapper<Books> {

    /**
     * 分页查询图书信息（关联作者信息）
     *
     * @param page    分页参数
     * @param request 查询条件
     * @return 图书信息列表
     */
    Page<BookResponse> selectBooksWithAuthor(@Param("page") Page<BookResponse> page, @Param("req") BookQueryRequest request);
}
