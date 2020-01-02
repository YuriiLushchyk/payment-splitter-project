package com.eleks.groupservice.converter;

import com.google.common.collect.Sets;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class LongSetToStringConverterTest {

    LongSetToStringConverter converter;

    @BeforeEach
    void setUp() {
        converter = new LongSetToStringConverter();
    }

    @Test
    void convertToDatabaseColumn_GivenListOfThreeLong_ReturnStringWithThreeDigits() {
        Set<Long> digits = Sets.newHashSet(1L, 2L, 20L);
        String converted = converter.convertToDatabaseColumn(digits);
        assertEquals("1;2;20", converted);
    }

    @Test
    void convertToDatabaseColumn_GivenEmptyList_ReturnNull() {
        Set<Long> digits = Collections.emptySet();
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

        Set<Long> digits = converter.convertToEntityAttribute(data);

        assertTrue(digits.contains(1L));
        assertTrue(digits.contains(2L));
        assertTrue(digits.contains(20L));
    }

    @Test
    void convertToEntityAttribute_GivenEmptyString_ReturnEmptyList() {
        Set<Long> digits = converter.convertToEntityAttribute("");
        assertTrue(digits.isEmpty());
    }

    @Test
    void convertToEntityAttribute_GivenNull_ReturnEmptyList() {
        Set<Long> digits = converter.convertToEntityAttribute(null);
        assertTrue(digits.isEmpty());
    }
}