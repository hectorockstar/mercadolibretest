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
        exceptionMessages.put("URL_EXPIRED", "La fecha de acceso a la Url ha expirado!");
        exceptionMessages.put("URL_EXIST", "La url que intentas registrar ya existe!");
        exceptionMessages.put("URL_NOT_AVAILABLE", "La url que intentas acceder no esta disponible o no existe!");

        EXCEPTION_MESSAGES = exceptionMessages;
    }
}
