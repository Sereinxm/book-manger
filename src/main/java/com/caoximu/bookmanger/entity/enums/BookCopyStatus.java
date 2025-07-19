package com.caoximu.bookmanger.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 图书副本状态枚举
 *
 * @author caoximu
 */
@Getter
@AllArgsConstructor
public enum BookCopyStatus {

    /**
     * 可借阅
     */
    AVAILABLE("available", "可借阅"),

    /**
     * 已借出
     */
    BORROWED("borrowed", "已借出");


    /**
     * 状态代码
     */
    private final String code;

    /**
     * 状态描述
     */
    private final String description;

    /**
     * 根据代码获取枚举
     *
     * @param code 状态代码
     * @return 枚举值
     */
    public static BookCopyStatus fromCode(String code) {
        for (BookCopyStatus status : values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        return null;
    }

    /**
     * 检查状态是否可借阅
     *
     * @param code 状态代码
     * @return true-可借阅，false-不可借阅
     */
    public static boolean isAvailable(String code) {
        return AVAILABLE.getCode().equals(code);
    }

    /**
     * 检查状态是否已借出
     *
     * @param code 状态代码
     * @return true-已借出，false-未借出
     */
    public static boolean isBorrowed(String code) {
        return BORROWED.getCode().equals(code);
    }

    /**
     * 获取所有可用状态的代码列表
     *
     * @return 状态代码列表
     */
    public static String[] getAllCodes() {
        return new String[]{
                AVAILABLE.getCode(),
                BORROWED.getCode()
        };
    }
} 