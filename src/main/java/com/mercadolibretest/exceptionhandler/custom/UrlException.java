package com.mercadolibretest.exceptionhandler.custom;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.HashMap;
import java.util.Map;
@Data
@EqualsAndHashCode(callSuper=false)
public class UrlException extends Exception{

    private static final Map<String, String> EXCEPTION_MESSAGES;

    private final String message;

    public static UrlException create(String exceptionType) {
        return new UrlException(EXCEPTION_MESSAGES.get(exceptionType));
    }

    static {
        Map<String, String> exceptionMessages = new HashMap<String, String>();
        exceptionMessages.put("INVALID_URL", "Lo sentimos! La URL que se esta intentando registrar no es valida!");

        EXCEPTION_MESSAGES = exceptionMessages;
    }
}
