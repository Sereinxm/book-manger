package com.caoximu.bookmanger.utils;

import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;

import java.util.Objects;

public class LoginHelper {

    public static Long getUserId() {
        SaTokenInfo tokenInfo = StpUtil.getTokenInfo();
        if (Objects.nonNull(tokenInfo)){
            Object loginId = tokenInfo.getLoginId();
            return Long.parseLong(loginId.toString());
        }
        return null;
    }
}