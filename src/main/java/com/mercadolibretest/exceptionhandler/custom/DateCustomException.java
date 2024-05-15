package com.mercadolibretest.exceptionhandler.custom;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class DateCustomException extends Exception {
    private static final Map<String, String> EXCEPTION_MESSAGES;

    private final String message;

    public static DateCustomException create(String exceptionType) {
        return new DateCustomException(EXCEPTION_MESSAGES.get(exceptionType));
    }

    static {
        Map<String, String> exceptionMessages = new HashMap<String, String>();
        exceptionMessages.put("DATE_BAD_PARSE", "Error al recepocionar fecha. La fecha debe obedecer el siguiente formato, ejemplo: '2024-05-15 22:37:00' !");
        exceptionMessages.put("DATE_EXPIRED", "La fecha y hora de expiracion no puede ser menor a la feche y hora actual!");

        EXCEPTION_MESSAGES = exceptionMessages;
    }
}
