package com.mercadolibretest.exceptionhandler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mercadolibretest.exceptionhandler.custom.UrlConfigActionException;
import com.mercadolibretest.exceptionhandler.custom.UrlException;
import com.mercadolibretest.utils.Utils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;


@ControllerAdvice
public class ExceptionAdvisor {

    @ExceptionHandler(UrlException.class)
    public ResponseEntity<String> handleUrlException(UrlException urlException){
        HttpStatus httpStatus = HttpStatus.ACCEPTED;
        Map<String, String> errorMap = new HashMap<String, String>();
        errorMap.put("mensaje", urlException.getMessage());

        return new ResponseEntity<>(Utils.toJSONFromObject(errorMap), httpStatus);
    }

    @ExceptionHandler(UrlConfigActionException.class)
    public ResponseEntity<String> handleUrlConfigActionException(UrlConfigActionException urlConfigActionException){
        HttpStatus httpStatus = HttpStatus.ACCEPTED;
        Map<String, String> errorMap = new HashMap<String, String>();
        errorMap.put("mensaje", urlConfigActionException.getMessage());

        return new ResponseEntity<>(Utils.toJSONFromObject(errorMap), httpStatus);
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<String> handleNullPointerException(NullPointerException ex) {
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        Map<String, String> errorMap = new HashMap<String, String>();
        errorMap.put("mensaje", ex.getMessage());

        return new ResponseEntity<>(Utils.toJSONFromObject(errorMap), httpStatus);

    }

    @ExceptionHandler(JsonProcessingException.class)
    public ResponseEntity<String> handleJsonProcessingException(JsonProcessingException ex) {
        String exText = ex.getLocalizedMessage();
        HttpStatus httpStatus = HttpStatus.ACCEPTED;
        return new ResponseEntity<>(exText, httpStatus);
    }

}
