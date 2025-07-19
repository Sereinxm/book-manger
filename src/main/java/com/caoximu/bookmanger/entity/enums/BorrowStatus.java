package com.caoximu.bookmanger.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 借阅状态枚举
 *
 * @author caoximu
 */
@Getter
@AllArgsConstructor
public enum BorrowStatus {

    /**
     * 借阅中
     */
    ACTIVE("active", "借阅中"),

    /**
     * 已归还
     */
    RETURNED("returned", "已归还"),

    /**
     * 逾期
     */
    OVERDUE("overdue", "逾期");

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
    public static BorrowStatus fromCode(String code) {
        for (BorrowStatus status : values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        return null;
    }

    /**
     * 检查状态是否为活跃借阅
     *
     * @param code 状态代码
     * @return true-活跃借阅，false-非活跃借阅
     */
    public static boolean isActive(String code) {
        return ACTIVE.getCode().equals(code);
    }

    /**
     * 检查状态是否已归还
     *
     * @param code 状态代码
     * @return true-已归还，false-未归还
     */
    public static boolean isReturned(String code) {
        return RETURNED.getCode().equals(code);
    }

    /**
     * 检查状态是否逾期
     *
     * @param code 状态代码
     * @return true-逾期，false-未逾期
     */
    public static boolean isOverdue(String code) {
        return OVERDUE.getCode().equals(code);
    }
}