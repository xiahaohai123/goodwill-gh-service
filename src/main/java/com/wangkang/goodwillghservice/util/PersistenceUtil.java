package com.wangkang.goodwillghservice.util;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public class PersistenceUtil {

    private PersistenceUtil() {
    }

    public static <T, K> List<T> updateOldData(
            List<T> newList,
            List<T> oldList,
            Function<T, K> keyExtractor,
            BiConsumer<T, T> updater) {

        Map<K, T> map = oldList.stream()
                .collect(Collectors.toMap(keyExtractor, Function.identity()));

        for (T newItem : newList) {
            K key = keyExtractor.apply(newItem);
            if (map.containsKey(key)) {
                T oldItem = map.get(key);
                updater.accept(oldItem, newItem);
            } else {
                oldList.add(newItem);
                map.put(key, newItem);
            }
        }
        return oldList;
    }
}
