package com.wangkang.wangkangdataetlservice.security;

import java.security.SecureRandom;

public class SecurePasswordGenerator {
    private SecurePasswordGenerator() {
    }

    // 可根据安全策略自行调整字符集
    private static final char[] CHARSET = (
            "ABCDEFGHIJKLMNOPQRSTUVWXYZ" +   // 大写字母
                    "abcdefghijklmnopqrstuvwxyz" +   // 小写字母
                    "0123456789" +                   // 数字
                    "!@#$%^&*"                       // 精简特殊字符
    ).toCharArray();
    private static final SecureRandom RANDOM = new SecureRandom();
    public static String generate(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int idx = RANDOM.nextInt(CHARSET.length);
            sb.append(CHARSET[idx]);
        }
        return sb.toString();
    }
}
