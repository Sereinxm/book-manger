package com.caoximu.bookmanger.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.caoximu.bookmanger.entity.SystemConfigs;
import com.caoximu.bookmanger.mapper.SystemConfigsMapper;
import com.caoximu.bookmanger.service.ISystemConfigsService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 系统配置表 服务实现类
 * </p>
 *
 * @author caoximu
 * @since 2025-07-19
 */
@Service
public class SystemConfigsServiceImpl extends ServiceImpl<SystemConfigsMapper, SystemConfigs> implements ISystemConfigsService {

}
