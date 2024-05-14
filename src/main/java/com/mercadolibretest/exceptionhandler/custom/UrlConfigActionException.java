package com.mercadolibretest.exceptionhandler.custom;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class UrlConfigActionException extends Exception {
    private static final Map<String, String> EXCEPTION_MESSAGES;

    private final String message;

    public static UrlConfigActionException create(String exceptionType) {
        return new UrlConfigActionException(EXCEPTION_MESSAGES.get(exceptionType));
    }

    static {
        Map<String, String> exceptionMessages = new HashMap<String, String>();
        exceptionMessages.put("DELETE", "La configuracion de URL que intenta eliminar no existe!");

        EXCEPTION_MESSAGES = exceptionMessages;
    }
}
