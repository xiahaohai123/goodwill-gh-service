package com.wangkang.wangkangdataetlservice.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class GsonUtils {
    private static final Gson GSON_INSTANCE = new GsonBuilder()
            // .setDateFormat("yyyy-MM-dd HH:mm:ss") // 可选
            // .excludeFieldsWithoutExposeAnnotation() // 可选
            .create();

    private GsonUtils() {
        // 防止外部实例化
    }

    public static Gson getGson() {
        return GSON_INSTANCE;
    }

    public static String toJson(Object obj) {
        return GSON_INSTANCE.toJson(obj);
    }

    /**
     * 将任意对象列表转换为 List<Map<String, Object>>。
     * @param collection 任意对象列表
     * @param <T>  泛型
     * @return List<Map < String, Object>>
     */
    public static <T> List<Map<String, Object>> toMapList(Collection<T> collection) {
        Gson gson = getGson();
        return collection.stream()
                .map(item -> gson.<Map<String, Object>>fromJson(gson.toJson(item),
                        new TypeToken<Map<String, Object>>() {
                        }.getType()))
                .toList();
    }
}
