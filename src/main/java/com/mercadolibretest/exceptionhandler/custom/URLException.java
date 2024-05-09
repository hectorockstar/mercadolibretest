package com.mercadolibretest.exceptionhandler.custom;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.HashMap;
import java.util.Map;
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
public class URLException extends Exception{

    private static final Map<String, String> EXCEPTION_MESSAGES;

    private final String message;

    public static URLException create(String exceptionType) {
        return new URLException(EXCEPTION_MESSAGES.get(exceptionType));
    }

    static {
        Map<String, String> exceptionMessages = new HashMap<String, String>();
        exceptionMessages.put("INVALID", "La URL que esta intentando acortar es invalida!");

        EXCEPTION_MESSAGES = exceptionMessages;
    }
}
