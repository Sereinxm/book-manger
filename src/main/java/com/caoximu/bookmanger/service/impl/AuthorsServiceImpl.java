package com.caoximu.bookmanger.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.caoximu.bookmanger.entity.Authors;
import com.caoximu.bookmanger.mapper.AuthorsMapper;
import com.caoximu.bookmanger.service.IAuthorsService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 作者表 服务实现类
 * </p>
 *
 * @author caoximu
 * @since 2025-07-19
 */
@Service
public class AuthorsServiceImpl extends ServiceImpl<AuthorsMapper, Authors> implements IAuthorsService {

}
