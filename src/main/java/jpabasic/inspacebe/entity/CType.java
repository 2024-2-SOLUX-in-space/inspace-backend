package jpabasic.inspacebe.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum CType {
    IMAGE,
    YOUTUBE,
    MUSIC,
    STICKER,
    SPACE;

    @JsonCreator
    public static CType fromValue(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Invalid CType value: " + value);
        }
        try {
            return CType.valueOf(value.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid CType value: " + value, e);
        }
    }

    @JsonValue
    public String toValue() {
        return name().toLowerCase(); // 반환 시 소문자로 반환
    }

    public static CType fromString(String value) {
        if (value == null) {
            throw new IllegalArgumentException("CType value cannot be null");
        }
        try {
            return CType.valueOf(value.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid CType value: " + value, e);
        }
    }


}
