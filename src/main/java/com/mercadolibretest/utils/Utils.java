package com.mercadolibretest.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mercadolibretest.exceptionhandler.custom.URLException;
import lombok.SneakyThrows;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.MessageFormat;

public class Utils {

    @SneakyThrows
    public static String toJSONFromObject(Object object) {
        return new ObjectMapper().writeValueAsString(object);
    }

    @SneakyThrows
    public static void urlValidator(String url) {
        try {
            new URL(url).toURI();
        } catch (MalformedURLException | URISyntaxException e) {
            throw URLException.create("INVALID");
        }
    }

}
