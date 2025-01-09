package jpabasic.inspacebe.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum CType {
    IMAGE,
    YOUTUBE,
    MUSIC,
    SPACE;

    @JsonCreator
    public static CType fromValue(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("CType value cannot be null or empty");
        }
        try {
            return CType.valueOf(value.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid CType value: " + value);
        }
    }

    @JsonValue
    public String toValue() {
        return name().toLowerCase(); // 반환 시 소문자로 반환
    }
}
