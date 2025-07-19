package com.caoximu.bookmanger.utils;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.digest.BCrypt;
import cn.hutool.crypto.digest.DigestUtil;

/**
 * 密码工具类
 * 
 * @author caoximu
 */
public class PasswordUtil {

    
    /**
     * 生成随机密码
     * 
     * @param length 密码长度
     * @return 随机密码
     */
    public static String generateRandomPassword(int length) {
        return RandomUtil.randomString(
            "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*", 
            length
        );
    }
    
    /**
     * 加密密码
     * 
     * @param password 原始密码
     * @return 加密后的密码
     */
    public static String encodePassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }
    public static String encryptPwd(String salt, String plainPwd) {
        String field = salt + "_" + plainPwd;
        String encPwd1 = DigestUtil.md5Hex(field);
        String encPwd2 = DigestUtil.md5Hex(encPwd1);
        return DigestUtil.md5Hex(encPwd2);
    }
    
    /**
     * 验证密码
     * 
     * @param password 原始密码
     * @param encodedPassword 加密后的密码
     * @return 是否匹配
     */
    public static boolean matches(String salt,String password, String encodedPassword) {
        return BCrypt.checkpw(encryptPwd(salt, password), BCrypt.hashpw(encodedPassword));
    }
} 