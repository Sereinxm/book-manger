package com.caoximu.bookmanger.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.caoximu.bookmanger.entity.Users;
import jakarta.validation.constraints.NotBlank;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 用户表 Mapper 接口
 * </p>
 *
 * @author caoximu
 * @since 2025-07-19
 */
@Mapper
public interface UsersMapper extends BaseMapper<Users> {

    default Users getAppUser(String email){
        return selectOne(Wrappers.<Users>lambdaQuery()
                .eq(Users::getEmail, email)
        );
    }
}
