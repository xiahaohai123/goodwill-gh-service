package com.wangkang.goodwillghservice.util;

import com.google.gson.JsonObject;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

/** 琐事 */
public class ChoreUtil {

    private ChoreUtil() {
    }

    public static JsonObject createFNumberAllCapitalGsonObject(String value) {
        JsonObject object = new JsonObject();
        object.addProperty("FNUMBER", value);
        return object;
    }

    public static JsonObject createFNumberGsonObject(String value) {
        JsonObject object = new JsonObject();
        object.addProperty("FNumber", value);
        return object;
    }

    public static String toString(Object object) {
        if (object == null) {
            return null;
        } else {
            return object.toString();
        }
    }

    public static BigDecimal toBigDecimal(Object object) {
        if (object == null) {
            return null;
        }
        String number = toString(object);
        return new BigDecimal(number);
    }

    public static Float toFloat(Object object) {
        if (object == null) {
            return null;
        }
        return toBigDecimal(object).floatValue();
    }

    public static Double toDouble(Object object) {
        if (object == null) {
            return null;
        }
        return toBigDecimal(object).doubleValue();
    }

    public static Integer toInteger(Object object) {
        if (object == null) {
            return null;
        }
        return toBigDecimal(object).intValue();
    }

    public static Long toLong(Object object) {
        if (object == null) {
            return null;
        }
        return toBigDecimal(object).longValue();
    }

    public static <K> Map<K, Double> mapBigDecimalValue2Double(Map<K, BigDecimal> source) {
        if (source == null) {
            return Collections.emptyMap();
        }
        return source.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> e.getValue() == null ? 0.0 : e.getValue().doubleValue()
                ));
    }

    public static <K> Map<K, BigDecimal> mapDoubleValue2BigDecimal(Map<K, Double> source) {
        if (source == null) {
            return Collections.emptyMap();
        }
        return source.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> e.getValue() == null ? BigDecimal.ZERO : BigDecimal.valueOf(e.getValue())
                ));
    }

}
