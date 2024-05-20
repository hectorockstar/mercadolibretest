package com.mercadolibretest.controller;

import com.mercadolibretest.dto.UrlDataRequest;
import com.mercadolibretest.dto.UrlDataResponse;
import com.mercadolibretest.model.UrlEntity;
import com.mercadolibretest.service.UrlManegementService;
import com.mercadolibretest.utils.Utils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.http.HttpResponse;

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
        UrlDataResponse urlDataResponse = urlManegementService.createShortUrl(urlDataRequest);
        return new ResponseEntity<>(Utils.toJSONFromObject(urlDataResponse), HttpStatus.CREATED);
    }

    @GetMapping("/get-url")
    @ResponseBody
    public ResponseEntity<Object> getUrlByUrl(@RequestParam("url") String url) {
        UrlDataResponse urlDataResponse = urlManegementService.getLongUrlByShortUrl(url);
        String urlResult = urlManegementService.showUrl(url, urlDataResponse);
        return ResponseEntity.ok(urlResult);
    }

    @GetMapping("/get-url-info")
    @ResponseBody
    public ResponseEntity<Object> getUrlInfoByUrl(@RequestParam("url") String url) {
        UrlDataResponse urlDataResponse = urlManegementService.getLongUrlByShortUrl(url);
        return ResponseEntity.ok(Utils.toJSONFromObject(urlDataResponse));
    }

    @GetMapping("/{shortUrl}")
    @ResponseBody
    public ResponseEntity<Object> redirectToLongUrlByShortUrl(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, @PathVariable String shortUrl) {
        UrlDataResponse urlDataResponse = urlManegementService.getLongUrlByShortUrl(shortUrl);
        urlManegementService.redirectToLonglUrlbyShortUrl(urlDataResponse, httpServletResponse);

        return new ResponseEntity<>(Utils.toJSONFromObject(urlDataResponse), HttpStatus.MOVED_TEMPORARILY);
    }

    @DeleteMapping("/{shortUrl}")
    @ResponseBody
    public ResponseEntity<Object> deleteUrlConfigByShortUrl(@PathVariable String shortUrl) {
        UrlEntity urlEntity = urlManegementService.deleteUrlConfigByShortUrl(shortUrl);
        return ResponseEntity.ok(String.format("Configuracion de URL: '%s' ha sido eliminada exitosamente", urlEntity.getLongUrl()));
    }

    @PatchMapping("/{shortUrl}")
    @ResponseBody
    public ResponseEntity<Object> updateUrlConfigByShortUrl(@PathVariable String shortUrl, @RequestBody UrlDataRequest urlDataRequest) {
        UrlDataResponse urlDataResponse = urlManegementService.updateUrlConfigByShortUrl(shortUrl, urlDataRequest);
        return ResponseEntity.ok(urlDataResponse);
    }

}
