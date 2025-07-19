package com.caoximu.bookmanger.entity.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

import java.math.BigDecimal;

/**
 * 系统配置类型枚举
 * 
 * @author caoximu
 */
@Getter
public enum SystemConfigType {
    
    /**
     * 默认借阅周期（天）
     */
    BORROW_DURATION_DAYS("borrow_duration_days", "30", "number", "默认借阅周期（天）"),
    
    /**
     * 单用户最大可借阅册数
     */
    MAX_BORROW_COUNT("max_borrow_count", "5", "number", "单用户最大可借阅册数"),
    
    /**
     * 逾期每日罚金（元）
     */
    OVERDUE_FINE_PER_DAY("overdue_fine_per_day", "0.5", "number", "逾期每日罚金（元）"),
    
    /**
     * 系统名称
     */
    SYSTEM_NAME("system_name", "图书管理系统", "string", "系统名称"),
    
    /**
     * 系统版本
     */
    SYSTEM_VERSION("system_version", "1.0.0", "string", "系统版本");
    
    @EnumValue
    private final String configKey;
    private final String defaultValue;
    private final String dataType;
    private final String description;
    
    SystemConfigType(String configKey, String defaultValue, String dataType, String description) {
        this.configKey = configKey;
        this.defaultValue = defaultValue;
        this.dataType = dataType;
        this.description = description;
    }

    /**
     * 根据配置键获取配置类型
     */
    public static SystemConfigType fromConfigKey(String configKey) {
        for (SystemConfigType type : values()) {
            if (type.getConfigKey().equals(configKey)) {
                return type;
            }
        }
        throw new IllegalArgumentException("未知的系统配置键: " + configKey);
    }
    
    /**
     * 判断是否为数字类型配置
     */
    public boolean isNumberType() {
        return "number".equals(this.dataType);
    }
    
    /**
     * 判断是否为字符串类型配置
     */
    public boolean isStringType() {
        return "string".equals(this.dataType);
    }
    
    /**
     * 获取配置的整数默认值（仅适用于数字类型）
     */
    public BigDecimal getNumDefaultValue() {
        if (!isNumberType()) {
            throw new IllegalStateException("配置 " + configKey + " 不是数字类型");
        }
        return new BigDecimal(defaultValue);
    }
    

} 