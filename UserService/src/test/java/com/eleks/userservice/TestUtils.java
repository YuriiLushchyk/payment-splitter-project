package com.eleks.userservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.util.Random;

public class TestUtils {

    public static String asJsonString(final Object obj) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            return mapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T jsonAsObject(final String json, Class<T> clazz) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            return mapper.readValue(json, clazz);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String getRandomString(int length) {
        String source = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890";
        Random random = new Random();
        char[] text = new char[length];
        for (int i = 0; i < length; i++) {
            text[i] = source.charAt(random.nextInt(source.length()));
        }
        return new String(text);
    }
}
