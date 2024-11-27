package com.coigniez.resumebuilder.model.sectionitem;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.SneakyThrows;

import java.util.Map;

@Converter
public class MapConverter implements AttributeConverter<Map<String, Object>, String> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    @SneakyThrows
    public String convertToDatabaseColumn(Map<String, Object> attribute) {
        return objectMapper.writeValueAsString(attribute);
    }

    @Override
    @SneakyThrows
    public Map<String, Object> convertToEntityAttribute(String dbData) {
        return objectMapper.readValue(dbData, new TypeReference<Map<String, Object>>() {});
    }
}
