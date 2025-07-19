package com.caoximu.bookmanger.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.caoximu.bookmanger.entity.BookCopies;
import com.caoximu.bookmanger.mapper.BookCopiesMapper;
import com.caoximu.bookmanger.service.IBookCopiesService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 图书副本表 服务实现类
 * </p>
 *
 * @author caoximu
 * @since 2025-07-19
 */
@Service
public class BookCopiesServiceImpl extends ServiceImpl<BookCopiesMapper, BookCopies> implements IBookCopiesService {

}
