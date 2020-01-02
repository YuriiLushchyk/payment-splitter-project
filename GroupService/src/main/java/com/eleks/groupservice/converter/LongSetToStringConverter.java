package com.eleks.groupservice.converter;

import lombok.extern.slf4j.Slf4j;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@Converter
@Slf4j
public class LongSetToStringConverter implements AttributeConverter<Set<Long>, String> {

    private static final String SPLIT_CHAR = ";";

    @Override
    public String convertToDatabaseColumn(Set<Long> attribute) {
        if (attribute == null || attribute.isEmpty()) return null;
        return attribute.stream().map(String::valueOf).collect(Collectors.joining(SPLIT_CHAR));
    }

    @Override
    public Set<Long> convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isEmpty()) return Collections.emptySet();
        try {
            return Arrays.stream(dbData.split(SPLIT_CHAR)).map(Long::valueOf).collect(Collectors.toSet());
        } catch (NumberFormatException ex) {
            log.info("exception during converting of String to List<Long>");
            return null;
        }
    }
}
