package com.caoximu.bookmanger.config;

import com.caoximu.bookmanger.service.impl.SystemInitServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * 系统启动时的初始化运行器
 * 负责在系统启动时执行必要的初始化操作
 * 
 * @author caoximu
 */
@Slf4j
@Component
@Order(1) // 设置优先级，确保在其他组件之前执行
@RequiredArgsConstructor
public class SystemBootstrapRunner implements CommandLineRunner {
    
    private final SystemInitServiceImpl systemInitService;
    
    @Override
    public void run(String... args) {
        try {
            log.info("系统启动初始化开始...");
            systemInitService.initializeSystem();
            log.info("系统启动初始化完成");
        } catch (Exception e) {
            log.error("系统启动初始化失败", e);
            throw new RuntimeException("系统启动初始化失败");
        }
    }
}