package com.caoximu.bookmanger.controller;

import com.caoximu.bookmanger.common.R;
import com.caoximu.bookmanger.common.annotation.RequireRole;
import com.caoximu.bookmanger.domain.request.UpdateSystemConfigRequest;
import com.caoximu.bookmanger.domain.response.SystemConfigResponse;
import com.caoximu.bookmanger.entity.enums.UserRole;
import com.caoximu.bookmanger.service.ISystemConfigsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 系统配置表 前端控制器
 * </p>
 *
 * @author caoximu
 * @since 2025-07-19
 */
@Slf4j
@Api(tags = "系统配置管理")
@RestController
@RequestMapping("/systemConfigs")
@RequiredArgsConstructor
public class SystemConfigsController {

    private final ISystemConfigsService systemConfigsService;

    /**
     * 获取系统所有参数
     */
    @ApiOperation("获取系统所有配置参数")
    @RequireRole(UserRole.ADMIN)
    @GetMapping("/list")
    public R<List<SystemConfigResponse>> getAllSystemConfigs() {
        log.info("开始获取系统所有配置参数");
        List<SystemConfigResponse> configList = systemConfigsService.getAllSystemConfigs();
        log.info("成功获取系统配置参数，共{}条", configList.size());
        return R.ok(configList);
    }

    /**
     * 修改系统参数
     */
    @ApiOperation("修改系统配置参数（管理员）")
    @RequireRole(UserRole.ADMIN)
    @PutMapping("/update")
    public R<Void> updateSystemConfig(@RequestBody @Validated UpdateSystemConfigRequest request) {
        log.info("开始更新系统配置参数，配置键：{}，配置值：{}", request.getConfigKey(), request.getConfigValue());
        systemConfigsService.updateSystemConfig(request);
        log.info("成功更新系统配置参数，配置键：{}", request.getConfigKey());
        return R.ok();
    }

}
