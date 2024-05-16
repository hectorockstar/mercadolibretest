package com.mercadolibretest.exceptionhandler.custom;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.HashMap;
import java.util.Map;

@Data
@EqualsAndHashCode(callSuper=false)
public class UrlConfigActionException extends Exception {
    private static final Map<String, String> EXCEPTION_MESSAGES;

    private final String message;

    public static UrlConfigActionException create(String exceptionType) {
        return new UrlConfigActionException(EXCEPTION_MESSAGES.get(exceptionType));
    }

    static {
        Map<String, String> exceptionMessages = new HashMap<String, String>();
        exceptionMessages.put("URL_NOT_EXIST", "La configuracion de URL que intenta eliminar no existe!");
        exceptionMessages.put("URL_EXPIRED", "Lo sentimos! La fecha de acceso a la Url ha expirado!");
        exceptionMessages.put("URL_EXIST", "Lo sentimos! La url que intentas registrar ya existe!");
        exceptionMessages.put("URL_NOT_AVAILABLE", "Lo sentimos! La url que intentas acceder o ver, ya no esta disponible o no existe!");

        EXCEPTION_MESSAGES = exceptionMessages;
    }
}
