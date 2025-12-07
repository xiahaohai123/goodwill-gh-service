package com.wangkang.goodwillghservice.dao.goodwillghservice.audit.model;

import com.wangkang.goodwillghservice.exception.BusinessException;
import com.wangkang.goodwillghservice.share.util.JacksonUtils;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class ObjectJsonConverter implements AttributeConverter<Object, String> {

    @Override
    public String convertToDatabaseColumn(Object attribute) {
        if (attribute == null) {
            return null; // 数据库里就是 null
        }
        try {
            return JacksonUtils.toJson(attribute);
        } catch (Exception e) {
            throw new BusinessException("序列化 JSON 出错: " + attribute, e);
        }
    }

    @Override
    public Object convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isBlank()) {
            return null; // 这里返回 null 更安全
        }
        try {
            return JacksonUtils.fromJson(dbData, Object.class);
        } catch (Exception e) {
            throw new BusinessException("反序列化 JSON 出错: " + dbData, e);
        }
    }
}