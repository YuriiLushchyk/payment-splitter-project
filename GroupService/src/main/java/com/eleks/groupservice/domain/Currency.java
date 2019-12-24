package com.eleks.groupservice.domain;

import com.eleks.groupservice.exception.EnumValidationException;
import com.fasterxml.jackson.annotation.JsonCreator;

public enum Currency {
    UAH, USD, EUR;

    @JsonCreator
    public static Currency create(String value) throws EnumValidationException {
        for (Currency currency : values()) {
            if (currency.name().equals(value)) {
                return currency;
            }
        }
        throw new EnumValidationException("Invalid currency value. Should be one of following : UAH, USD, EUR");
    }
}
