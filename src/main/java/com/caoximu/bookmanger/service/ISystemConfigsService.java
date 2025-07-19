package com.caoximu.bookmanger.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.caoximu.bookmanger.domain.request.UpdateSystemConfigRequest;
import com.caoximu.bookmanger.domain.response.SystemConfigResponse;
import com.caoximu.bookmanger.entity.SystemConfigs;
import com.caoximu.bookmanger.entity.enums.SystemConfigType;

import java.util.List;

/**
 * <p>
 * 系统配置表 服务类
 * </p>
 *
 * @author caoximu
 * @since 2025-07-19
 */
public interface ISystemConfigsService extends IService<SystemConfigs> {

    /**
     * 获取系统配置
     *
     * @return 系统配置
     */
    Object  getSystemConfig(SystemConfigType config);

    /**
     * 获取所有系统配置
     *
     * @return 所有系统配置列表
     */
    List<SystemConfigResponse> getAllSystemConfigs();

    /**
     * 更新系统配置
     *
     * @param request 更新请求
     */
    void updateSystemConfig(UpdateSystemConfigRequest request);

}
