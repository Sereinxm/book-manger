package com.caoximu.bookmanger.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.caoximu.bookmanger.entity.SystemConfigs;
import com.caoximu.bookmanger.entity.Users;
import com.caoximu.bookmanger.entity.enums.SystemConfigType;
import com.caoximu.bookmanger.entity.enums.UserRole;
import com.caoximu.bookmanger.service.ISystemConfigsService;
import com.caoximu.bookmanger.service.IUsersService;
import com.caoximu.bookmanger.utils.PasswordUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

/**
 * 系统初始化服务实现类
 * 
 * @author caoximu
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SystemInitServiceImpl {
    
    private final ISystemConfigsService systemConfigsService;
    private final IUsersService usersService;
    
    /**
     * 执行系统初始化
     */
    @Transactional(rollbackFor = Exception.class)
    public void initializeSystem() {
        log.info("开始执行系统初始化...");
        
        // 1. 初始化系统配置
        initSystemConfigs();
        
        // 2. 初始化超级管理员
        initSuperAdmin();
        
        log.info("系统初始化完成");
    }
    
    /**
     * 初始化系统配置
     */
    private void initSystemConfigs() {
        log.info("检查系统配置...");
        
        // 检查系统配置表是否为空
        long count = systemConfigsService.count();
        if (count == 0) {
            log.info("系统配置表为空，开始注入默认配置...");
            
            List<SystemConfigs> defaultConfigs = createDefaultConfigs();
            systemConfigsService.saveBatch(defaultConfigs);
            
            log.info("默认系统配置注入完成，共注入 {} 项配置", defaultConfigs.size());
        } else {
            log.info("系统配置表已存在数据，跳过默认配置注入");
        }
    }
    
    /**
     * 创建默认系统配置
     */
    private List<SystemConfigs> createDefaultConfigs() {
        LocalDateTime now = LocalDateTime.now();
        
        return Arrays.asList(
            createConfigFromEnum(SystemConfigType.BORROW_DURATION_DAYS, now),
            createConfigFromEnum(SystemConfigType.MAX_BORROW_COUNT, now),
            createConfigFromEnum(SystemConfigType.OVERDUE_FINE_PER_DAY, now),
            createConfigFromEnum(SystemConfigType.SYSTEM_NAME, now),
            createConfigFromEnum(SystemConfigType.SYSTEM_VERSION, now)
        );
    }
    
    /**
     * 从枚举创建配置项
     */
    private SystemConfigs createConfigFromEnum(SystemConfigType configType, LocalDateTime now) {
        SystemConfigs config = new SystemConfigs();
        config.setConfigKey(configType.getConfigKey());
        config.setConfigValue(configType.getDefaultValue());
        config.setConfigType(configType.getDataType());
        config.setDescription(configType.getDescription());
        config.setIsEnabled(true);
        config.setCreateTime(now);
        config.setUpdateTime(now);
        return config;
    }
    
    /**
     * 创建配置项
     */
    private SystemConfigs createConfig(String key, String value, String type, String description, LocalDateTime now) {
        SystemConfigs config = new SystemConfigs();
        config.setConfigKey(key);
        config.setConfigValue(value);
        config.setConfigType(type);
        config.setDescription(description);
        config.setIsEnabled(true);
        config.setCreateTime(now);
        config.setUpdateTime(now);
        return config;
    }
    
    /**
     * 初始化超级管理员
     */
    private void initSuperAdmin() {
        log.info("检查超级管理员账户...");

        long superAdminCount = usersService.count(Wrappers.<Users>lambdaQuery()
                .eq(Users::getRole, UserRole.SUPER_ADMIN.getCode())
                .eq(Users::getIsActive, true)
        );
        
        if (superAdminCount == 0) {
            log.info("未找到超级管理员账户，开始创建默认超级管理员...");
            
            String adminPassword = createSuperAdmin();
            
            log.warn("=======================");
            log.warn("重要提醒：默认超级管理员账户已创建");
            log.warn("账户邮箱: admin@system.com");
            log.warn("初始密码: {}", adminPassword);
            log.warn("请妥善保管此密码，并在首次登录后立即修改!");
            log.warn("==========================");
        } else {
            log.info("超级管理员账户已存在，跳过创建");
        }
    }
    
    /**
     * 创建超级管理员账户
     */
    private String createSuperAdmin() {
        // 生成随机密码
//        String randomPassword = PasswordUtil.generateRandomPassword(12);
        String randomPassword = "123456";
        
        LocalDateTime now = LocalDateTime.now();
        String salt = "admin";
        String password = PasswordUtil.encryptPwd(salt, randomPassword);
        Users superAdmin = new Users();
        superAdmin.setName("系统管理员");
        superAdmin.setEmail("admin@system.com");
        superAdmin.setPassword(password);
        superAdmin.setRole(UserRole.SUPER_ADMIN.getCode());
        superAdmin.setJoinDate(now);
        superAdmin.setSalt(salt);
        usersService.save(superAdmin);
        
        log.info("默认超级管理员账户创建完成");
        return randomPassword;
    }
} 