package com.wangkang.goodwillghservice.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.hibernate6.Hibernate6Module;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.wangkang.goodwillghservice.exception.BusinessException;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class JacksonUtils {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    static {
        // 注册 Java 8 时间模块 (支持 OffsetDateTime, LocalDateTime 等)
        OBJECT_MAPPER.registerModule(new JavaTimeModule());
        // 关闭时间戳写法，改为 ISO-8601
        OBJECT_MAPPER.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        OBJECT_MAPPER.registerModule(new Jdk8Module());
        OBJECT_MAPPER.registerModule(new ParameterNamesModule());
        Hibernate6Module module = new Hibernate6Module();
        // 不要强制懒加载（否则可能触发 LazyInitializationException）
        module.disable(Hibernate6Module.Feature.FORCE_LAZY_LOADING);
        // 懒加载未初始化时，至少序列化 ID，避免 ByteBuddyInterceptor 错误
        module.enable(Hibernate6Module.Feature.SERIALIZE_IDENTIFIER_FOR_LAZY_NOT_LOADED_OBJECTS);
        // 遇到集合时，如果未初始化，直接序列化为空集合
        module.enable(Hibernate6Module.Feature.REPLACE_PERSISTENT_COLLECTIONS);
        module.enable(Hibernate6Module.Feature.WRITE_MISSING_ENTITIES_AS_NULL);
        OBJECT_MAPPER.registerModule(module);
    }

    private JacksonUtils() {
        // 防止外部实例化
    }

    public static ObjectMapper getObjectMapper() {
        return OBJECT_MAPPER;
    }

    public static String toJson(Object obj) {
        try {
            return OBJECT_MAPPER.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new BusinessException("Jackson 序列化失败", e);
        }
    }

    /**
     * 将任意对象列表转换为 List<Map<String, Object>>。
     */
    public static <T> List<Map<String, Object>> toMapList(Collection<T> collection) {
        return collection.stream()
                .map(item -> OBJECT_MAPPER.convertValue(item, new TypeReference<Map<String, Object>>() {
                }))
                .toList();
    }

    /**
     * 将 JSON 字符串反序列化为对象。
     */
    public static <T> T fromJson(String json, Class<T> clazz) {
        try {
            return OBJECT_MAPPER.readValue(json, clazz);
        } catch (JsonProcessingException e) {
            throw new BusinessException("Jackson 反序列化失败", e);
        }
    }
}
