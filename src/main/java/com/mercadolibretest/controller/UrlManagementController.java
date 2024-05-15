package com.mercadolibretest.controller;

import com.mercadolibretest.model.UrlDataRequest;
import com.mercadolibretest.model.UrlEntity;
import com.mercadolibretest.service.UrlManegementService;
import com.mercadolibretest.utils.Utils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jdk.jshell.execution.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;

@RestController
@RequestMapping("/url-management")
public class UrlManagementController {

    private final UrlManegementService urlManegementService;

    @Autowired
    public UrlManagementController(UrlManegementService urlManegementService) {
        this.urlManegementService = urlManegementService;
    }

    @PostMapping("/create-short-url")
    @ResponseBody
    public ResponseEntity<Object> createShortUrl(@RequestBody UrlDataRequest urlDataRequest) {
        return ResponseEntity.ok(Utils.toJSONFromObject(urlManegementService.createShortUrl(urlDataRequest)));
    }

    @GetMapping("/get-long-url")
    @ResponseBody
    public ResponseEntity<Object> getLongUrlByShortUrl(@RequestParam("shortUrl") String shortUrl) {
        return ResponseEntity.ok(Utils.toJSONFromObject(urlManegementService.getLongUrlByShortUrl(shortUrl)));
    }

    @GetMapping("/{shortUrl}")
    @ResponseBody
    public void redirectToLonglUrlbyShortUrl(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, @PathVariable String shortUrl) {
        urlManegementService.redirectToLonglUrlbyShortUrl(shortUrl, httpServletResponse);
    }

    @DeleteMapping("/{shortUrl}")
    @ResponseBody
    public ResponseEntity<Object> deleteUrlConfigByShortUrl(@PathVariable String shortUrl) {
        UrlEntity urlEntity = urlManegementService.deleteUrlConfigByShortUrl(shortUrl);
        return ResponseEntity.ok(String.format("Configuracion de URL: '%s' ha sido eliminada exitosamente", urlEntity.getLongUrl()));
    }

}
