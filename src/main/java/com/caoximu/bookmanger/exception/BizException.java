package com.caoximu.bookmanger.exception;

import com.caoximu.bookmanger.common.R;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;

@Slf4j
public class BizException extends BaseBizException {
    /**
     * 简体错误信息
     */
    private final String msg;

    public BizException(int code, String msg, Object... args) {
        super(code, msg, args, null);
        this.msg = msg;
    }

    public BizException(String msg) {
        this(R.FAIL, msg);
    }


    @Override
    public String getErrorMessage() {
        return formatMaybeWithArgs(msg, super.getMsgParams());
    }

    private String formatMaybeWithArgs(String str, Object[] args) {
        if (ArrayUtils.isNotEmpty(args)) {
            for (int i = 0; i < args.length; ++i) {
                String target = "{{" + i + "}}";
                if (str.contains(target)) {
                    str = str.replace(target, args[i].toString());
                }
            }
        }
        return str;
    }
}