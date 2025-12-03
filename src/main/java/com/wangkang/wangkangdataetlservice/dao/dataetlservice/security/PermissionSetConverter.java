package com.wangkang.wangkangdataetlservice.dao.dataetlservice.security;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wangkang.wangkangdataetlservice.dao.dataetlservice.security.model.Permission;
import com.wangkang.wangkangdataetlservice.dao.exception.ConvertException;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

@Converter
public class PermissionSetConverter implements AttributeConverter<Set<Permission>, String> {
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(Set<Permission> attribute) {
        if (attribute == null || attribute.isEmpty()) return "[]";
        try {
            return mapper.writeValueAsString(attribute);
        } catch (JsonProcessingException e) {
            throw new ConvertException("Could not serialize permissions", e);
        }
    }

    @Override
    public Set<Permission> convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isEmpty()) return new HashSet<>();
        try {
            return mapper.readValue(dbData,
                    mapper.getTypeFactory().constructCollectionType(Set.class, Permission.class));
        } catch (IOException e) {
            throw new ConvertException("Could not deserialize permissions", e);
        }
    }
}
