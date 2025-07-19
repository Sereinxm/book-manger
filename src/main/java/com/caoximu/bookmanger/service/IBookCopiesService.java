package com.caoximu.bookmanger.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.caoximu.bookmanger.domain.response.BookCopyResponse;
import com.caoximu.bookmanger.entity.BookCopies;

import java.util.List;

/**
 * <p>
 * 图书副本表 服务类
 * </p>
 *
 * @author caoximu
 * @since 2025-07-19
 */
public interface IBookCopiesService extends IService<BookCopies> {

    /**
     * 为图书创建指定数量的副本
     *
     * @param isbn 图书ISBN
     * @param count 副本数量
     * @param location 存放位置
     * @param conditionNotes 状态备注
     * @return 创建的副本列表
     */
    void createCopiesForBook(String isbn, int count, String location, String conditionNotes);

    /**
     * 根据ISBN获取所有副本
     *
     * @param isbn 图书ISBN
     * @return 副本列表
     */
    List<BookCopyResponse> getCopiesByIsbn(String isbn);

    /**
     * 检查图书是否有借出的副本
     *
     * @param isbn 图书ISBN
     * @return true-有借出的副本，false-没有借出的副本
     */
    boolean hasBorrowedCopies(String isbn);


    /**
     * 根据ISBN删除所有副本（逻辑删除）
     *
     * @param isbn 图书ISBN
     */
    void deleteAllCopiesByIsbn(String isbn);

    /**
     * 更新副本状态
     *
     * @param copyId 副本ID
     * @param status 新状态
     */
    void updateCopyStatus(Long copyId, String status);

    /**
     * 将BookCopies实体转换为DTO
     *
     * @param bookCopy 实体对象
     * @return DTO对象
     */
    BookCopyResponse convertToDto(BookCopies bookCopy);

    /**
     * 批量将BookCopies实体转换为DTO
     *
     * @param bookCopies 实体列表
     * @return DTO列表
     */
    List<BookCopyResponse> convertToDtos(List<BookCopies> bookCopies);
}
