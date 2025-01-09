package jpabasic.inspacebe.converter;

import jakarta.persistence.AttributeConverter;

public class CtypeAttributeConverter implements AttributeConverter <String,Integer>{
    @Override
    public Integer convertToDatabaseColumn(String attribute) {
        if ("image".equals(attribute)) {
            return 1;
        }else if ("youtube".equals(attribute)) {
            return 2;
        }else if ("music".equals(attribute)) {
            return 3;
        }
        return 4;
    }

    @Override
    public String convertToEntityAttribute(Integer dbData) {
        if (dbData == 1) {
            return "image";
        }else if(dbData == 2){
            return "youtube";
        }else if(dbData == 3){
            return "music";
        }
        return "space";
    }
}
