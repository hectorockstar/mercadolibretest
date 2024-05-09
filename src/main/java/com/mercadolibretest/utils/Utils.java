package com.mercadolibretest.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;

public class Utils {

    @SneakyThrows
    public static String toJSONFromObject(Object object) {
        return new ObjectMapper().writeValueAsString(object);
    }

}
