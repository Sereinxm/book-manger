package com.caoximu.bookmanger.service.impl;

import com.caoximu.bookmanger.entity.Users;
import com.caoximu.bookmanger.mapper.UsersMapper;
import com.caoximu.bookmanger.service.IUsersService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author caoximu
 * @since 2025-07-19
 */
@Service
public class UsersServiceImpl extends ServiceImpl<UsersMapper, Users> implements IUsersService {

}
