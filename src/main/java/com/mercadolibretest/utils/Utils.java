package com.mercadolibretest.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mercadolibretest.exceptionhandler.custom.UrlException;
import lombok.SneakyThrows;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

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
            throw UrlException.create("INVALID");
        }
    }

}
