package com.mercadolibretest.exceptionhandler.custom;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;
@Data
public class UrlException extends Exception{

    private static final Map<String, String> EXCEPTION_MESSAGES;

    private final String message;

    public static UrlException create(String exceptionType) {
        return new UrlException(EXCEPTION_MESSAGES.get(exceptionType));
    }

    static {
        Map<String, String> exceptionMessages = new HashMap<String, String>();
        exceptionMessages.put("INVALID", "La URL que esta intentando acortar es invalida!");

        EXCEPTION_MESSAGES = exceptionMessages;
    }
}
