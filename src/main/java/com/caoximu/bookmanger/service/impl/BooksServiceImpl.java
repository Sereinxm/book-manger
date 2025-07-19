package com.caoximu.bookmanger.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.caoximu.bookmanger.entity.Books;
import com.caoximu.bookmanger.mapper.BooksMapper;
import com.caoximu.bookmanger.service.IBooksService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 图书表 服务实现类
 * </p>
 *
 * @author caoximu
 * @since 2025-07-19
 */
@Service
public class BooksServiceImpl extends ServiceImpl<BooksMapper, Books> implements IBooksService {

}
