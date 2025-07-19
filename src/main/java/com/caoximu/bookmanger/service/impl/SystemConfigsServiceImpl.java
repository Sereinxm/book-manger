package com.caoximu.bookmanger.service.impl;

import cn.hutool.core.util.BooleanUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.caoximu.bookmanger.domain.request.UpdateSystemConfigRequest;
import com.caoximu.bookmanger.domain.response.SystemConfigResponse;
import com.caoximu.bookmanger.entity.SystemConfigs;
import com.caoximu.bookmanger.entity.enums.SystemConfigType;
import com.caoximu.bookmanger.exception.BizException;
import com.caoximu.bookmanger.mapper.SystemConfigsMapper;
import com.caoximu.bookmanger.service.ISystemConfigsService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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

    @Resource
    private SystemConfigsMapper systemConfigsMapper;

    @Override
    public Object  getSystemConfig(SystemConfigType config) {
        SystemConfigs systemConfigs = systemConfigsMapper.selectOne(Wrappers.<SystemConfigs>lambdaQuery()
                .eq(SystemConfigs::getConfigKey, config.getConfigKey())
        );
        String configValue = systemConfigs.getConfigValue();
        if (config.isNumberType()) {
            return new BigDecimal(configValue);
        } else if (config.isStringType()) {
            return configValue;
        } else {
            return BooleanUtil.toBoolean(configValue);
        }
    }

    @Override
    public List<SystemConfigResponse> getAllSystemConfigs() {
        List<SystemConfigs> configList = systemConfigsMapper.selectList(null);
        return configList.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void updateSystemConfig(UpdateSystemConfigRequest request) {
        // 验证配置键是否存在
        SystemConfigType.fromConfigKey(request.getConfigKey());
        
        // 查找现有配置
        SystemConfigs existingConfig = systemConfigsMapper.selectOne(
                Wrappers.<SystemConfigs>lambdaQuery()
                        .eq(SystemConfigs::getConfigKey, request.getConfigKey())
        );
        
        if (existingConfig == null) {
            throw new BizException("系统配置不存在: " + request.getConfigKey());
        }
        
        // 更新配置
        existingConfig.setConfigValue(request.getConfigValue());
        existingConfig.setIsEnabled(request.getIsEnabled());
        existingConfig.setUpdateTime(LocalDateTime.now());
        
        systemConfigsMapper.updateById(existingConfig);
    }

    /**
     * 转换实体为响应对象
     */
    private SystemConfigResponse convertToResponse(SystemConfigs config) {
        return SystemConfigResponse.builder()
                .id(config.getId())
                .configKey(config.getConfigKey())
                .configValue(config.getConfigValue())
                .configType(config.getConfigType())
                .description(config.getDescription())
                .isEnabled(config.getIsEnabled())
                .createTime(config.getCreateTime())
                .updateTime(config.getUpdateTime())
                .build();
    }
}
