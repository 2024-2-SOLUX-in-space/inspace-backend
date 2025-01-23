package jpabasic.inspacebe.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import jpabasic.inspacebe.entity.CType;

@Converter(autoApply = true)
public class CTypeConverter implements AttributeConverter<CType, String> {

    @Override
    public String convertToDatabaseColumn(CType attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.name().toUpperCase();  // Enum 값을 대문자 문자열로 변환
    }

    @Override
    public CType convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isBlank()) {
            return null;
        }
        try {
            return CType.valueOf(dbData.trim().toUpperCase()); // 대소문자 무시하고 매핑
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid CType value from database: " + dbData, e);
        }
    }
}