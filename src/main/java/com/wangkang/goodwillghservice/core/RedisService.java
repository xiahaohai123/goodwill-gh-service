package com.wangkang.goodwillghservice.core;


import com.wangkang.goodwillghservice.share.locale.MessageService;
import com.wangkang.goodwillghservice.share.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.connection.RedisKeyCommands;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;


@Service
public class RedisService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final HashOperations<String, String, Object> hashOperations;
    public static final String UPDATED_AT = "updatedAt";
    public static final String CREATED_AT = "createdAt";

    public static final String KEY_PREFIX = "GoodWill:";
    private final MessageService messageService;

    @Autowired
    public RedisService(RedisTemplate<String, Object> redisTemplate, MessageService messageService) {
        this.redisTemplate = redisTemplate;
        this.hashOperations = redisTemplate.opsForHash();
        this.messageService = messageService;
    }

    public List<String> scanKeys(String prefixPattern) {
        return redisTemplate.execute((RedisCallback<List<String>>) connection -> {
            RedisKeyCommands keyCmds = connection.keyCommands();
            ScanOptions options = ScanOptions.scanOptions()
                    .match(prefixPattern)
                    .count(500)
                    .build();
            List<String> result = new ArrayList<>();
            try (Cursor<byte[]> cursor = keyCmds.scan(options)) {
                cursor.forEachRemaining(bs -> result.add(new String(bs, StandardCharsets.UTF_8)));
            }
            return result;
        });
    }

    public List<String> scanHashKeys(String prefixPattern) {
        return redisTemplate.execute((RedisCallback<List<String>>) connection -> {
            RedisKeyCommands keyCmds = connection.keyCommands();
            ScanOptions options = ScanOptions.scanOptions()
                    .match(prefixPattern)
                    .count(500)
                    .build();
            List<String> result = new ArrayList<>();
            try (Cursor<byte[]> cursor = keyCmds.scan(options)) {
                cursor.forEachRemaining(bs -> {
                    DataType type = connection.keyCommands().type(bs);
                    if (type == DataType.HASH) {
                        result.add(new String(bs, StandardCharsets.UTF_8));
                    }
                });
            }
            return result;
        });
    }

    public void updateTime4opsHash(String key) {
        hashOperations.put(key, UPDATED_AT, DateUtil.currentYMDHMS());
    }

    public void createTime4opsHash(String key) {
        hashOperations.put(key, CREATED_AT, DateUtil.currentYMDHMS());
    }

    public void expire(String key, Duration duration) {
        redisTemplate.expire(key, duration);
    }

    public void delete(String key) {
        redisTemplate.delete(key);
    }

    /**
     * 创建完整的 Redis key，自动加上前缀，并用 ':' 拼接多个部分
     *
     * @param parts key 的各个部分，不定长参数
     * @return 完整的 key
     */
    public String buildKey(String... parts) {
        if (parts == null || parts.length == 0) {
            String message = messageService.getMessage("error.key.build.required");
            throw new IllegalArgumentException(message);
        }
        StringBuilder sb = new StringBuilder(KEY_PREFIX);
        for (int i = 0; i < parts.length; i++) {
            if (i > 0) {
                sb.append(":");  // 分隔符
            }
            sb.append(parts[i]);
        }
        return sb.toString();
    }
}
