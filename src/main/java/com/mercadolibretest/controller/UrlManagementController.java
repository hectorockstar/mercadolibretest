package com.mercadolibretest.controller;

import com.mercadolibretest.dto.UrlDataRequest;
import com.mercadolibretest.dto.UrlDataResponse;
import com.mercadolibretest.model.UrlEntity;
import com.mercadolibretest.service.UrlManegementService;
import com.mercadolibretest.utils.Utils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/get-url")
    @ResponseBody
    public ResponseEntity<Object> getUrlByUrl(@RequestParam("url") String url) {
        UrlDataResponse urlDataResponse = urlManegementService.getLongUrlByShortUrl(url);
        return ResponseEntity.ok(urlManegementService.showUrl(url, urlDataResponse));
    }

    @GetMapping("/get-url-info")
    @ResponseBody
    public ResponseEntity<Object> getInfoUrlByUrl(@RequestParam("url") String url) {
        return ResponseEntity.ok(Utils.toJSONFromObject(urlManegementService.getLongUrlByShortUrl(url)));
    }

    @GetMapping("/{shortUrl}")
    @ResponseBody
    public void redirectToLonglUrlbyShortUrl(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, @PathVariable String shortUrl) {
        UrlDataResponse urlDataResponse = urlManegementService.getLongUrlByShortUrl(shortUrl);
        urlManegementService.redirectToLonglUrlbyShortUrl(urlDataResponse, httpServletResponse);
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
        return ResponseEntity.ok(urlManegementService.updateUrlConfigByShortUrl(shortUrl, urlDataRequest));
    }

}
