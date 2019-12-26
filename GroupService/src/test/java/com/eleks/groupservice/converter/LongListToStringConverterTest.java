package com.eleks.groupservice.converter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class LongListToStringConverterTest {

    LongListToStringConverter converter;

    @BeforeEach
    void setUp() {
        converter = new LongListToStringConverter();
    }

    @Test
    void convertToDatabaseColumn_GivenListOfThreeLong_ReturnStringWithThreeDigits() {
        List<Long> digits = Arrays.asList(1L, 2L, 20L);
        String converted = converter.convertToDatabaseColumn(digits);
        assertEquals("1;2;20", converted);
    }

    @Test
    void convertToDatabaseColumn_GivenEmptyList_ReturnNull() {
        List<Long> digits = Collections.emptyList();
        String converted = converter.convertToDatabaseColumn(digits);
        assertNull(converted);
    }

    @Test
    void convertToDatabaseColumn_GivenNull_ReturnNull() {
        String converted = converter.convertToDatabaseColumn(null);
        assertNull(converted);
    }

    @Test
    void convertToEntityAttribute_GivenStringsWithIds_ReturnListOfLongs() {
        String data = "1;2;20";

        List<Long> digits = converter.convertToEntityAttribute(data);

        assertEquals(1L, digits.get(0));
        assertEquals(2L, digits.get(1));
        assertEquals(20L, digits.get(2));
    }

    @Test
    void convertToEntityAttribute_GivenEmptyString_ReturnNull() {
        List<Long> digits = converter.convertToEntityAttribute("");
        assertNull(digits);
    }

    @Test
    void convertToEntityAttribute_GivenNull_ReturnNull() {
        List<Long> digits = converter.convertToEntityAttribute(null);
        assertNull(digits);
    }
}