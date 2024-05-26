package com.mercadolibretest.utils;

import com.mercadolibretest.constants.MercadoLibreTestConstants;
import com.mercadolibretest.exceptionhandler.custom.UrlException;
import lombok.SneakyThrows;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

public class UrlUtils {

    @SneakyThrows
    public static void urlValidator(String url) {
        try {
            new URL(url).toURI();
        } catch (MalformedURLException | URISyntaxException e) {
            throw UrlException.create("INVALID_URL");
        }
    }

    public static Boolean isLongUrl(String url) {
        return url != null && url.length() > MercadoLibreTestConstants.URL_MAX_SIZE;
    }
}
