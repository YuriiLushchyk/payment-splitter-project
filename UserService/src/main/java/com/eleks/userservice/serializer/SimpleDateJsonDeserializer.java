package com.eleks.userservice.serializer;

import com.eleks.userservice.exception.InvalidDateFormatException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class SimpleDateJsonDeserializer extends JsonDeserializer<LocalDate> {
    public static final String PATTERN = "dd-MM-yyyy";

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(PATTERN);

    @Override
    public LocalDate deserialize(JsonParser jsonParser,
                                 DeserializationContext deserializationContext) throws IOException {
        try {
            return LocalDate.parse(jsonParser.getText(), formatter);
        } catch (Exception e) {
            String msg = String.format("Incorrect date format of %s. Valid pattern is %s", jsonParser.getCurrentName(), PATTERN);
            throw new InvalidDateFormatException(msg);
        }
    }
}
