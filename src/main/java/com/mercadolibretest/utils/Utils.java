package com.mercadolibretest.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mercadolibretest.constants.MercadoLibreTestConstants;
import com.mercadolibretest.exceptionhandler.custom.DateCustomException;
import com.mercadolibretest.exceptionhandler.custom.UrlException;
import lombok.SneakyThrows;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import static java.time.temporal.ChronoUnit.MINUTES;

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

    public static Boolean isLongUrl(String url) {
        return url != null && url.length() > MercadoLibreTestConstants.URL_MAX_SIZE;
    }

    @SneakyThrows
    public static Boolean expiredDateValidator(Date date) {
        long result = MINUTES.between(LocalDateTime.now(), dateToLocalDateFormat(date));
        if(result < 0) {
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    @SneakyThrows
    public static Date stringDateToDateFormatter(String stringDate) {
        Date date = null;
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            date = simpleDateFormat.parse(stringDate);
        } catch (ParseException e) {
            throw DateCustomException.create("DATE_BAD_PARSE");
        }
        return date;
    }

    @SneakyThrows
    public static String dateToStringDateFormatter(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return simpleDateFormat.format(date);
    }

    public static Date getSystemDate() throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String systemDate = simpleDateFormat.format(new Date());
        return simpleDateFormat.parse(systemDate);
    }

    public static LocalDateTime dateToLocalDateFormat(Date date) {
        return date.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }

    @SneakyThrows
    public static String getFileContentResource(String classpath){
        return new String(Files.readAllBytes(Paths.get(new ClassPathResource(classpath).getFile().toURI())));
    }

    @SneakyThrows
    public static Object toObjectFromJSON(String jsonString, Class<?> someClass) {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(jsonString, someClass);
    }

}
