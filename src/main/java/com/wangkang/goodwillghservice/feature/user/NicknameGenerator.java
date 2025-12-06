package com.wangkang.goodwillghservice.feature.user;

import java.security.SecureRandom;

public class NicknameGenerator {
    private NicknameGenerator() {
    }

    private static final String PREFIX = "user_";
    private static final String CHAR_POOL = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int RANDOM_LENGTH = 6;
    private static final SecureRandom RANDOM = new SecureRandom();

    /**
     * 生成随机昵称，如果传入的nickname不为空则返回原值
     * @return 最终昵称
     */
    public static String generateNickname() {
        return PREFIX + randomString();
    }

    /**
     * 生成指定长度的随机字符串
     */
    private static String randomString() {
        StringBuilder sb = new StringBuilder(NicknameGenerator.RANDOM_LENGTH);
        for (int i = 0; i < NicknameGenerator.RANDOM_LENGTH; i++) {
            sb.append(CHAR_POOL.charAt(RANDOM.nextInt(CHAR_POOL.length())));
        }
        return sb.toString();
    }
}
