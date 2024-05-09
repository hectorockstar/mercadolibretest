package com.mercadolibretest.controller;

import com.mercadolibretest.model.UrlEntity;
import com.mercadolibretest.service.UrlManegementService;
import com.mercadolibretest.utils.Utils;
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

    @GetMapping("/get-short-url")
    @ResponseBody
    public ResponseEntity<Object> getShortUrl(@RequestParam("url") String url) {

        UrlEntity urlEntity = urlManegementService.getShortUrl(url);
        return ResponseEntity.ok(Utils.toJSONFromObject(urlEntity));
    }

    @GetMapping("/get-long-url")
    @ResponseBody
    public ResponseEntity<Object> getLongUrl() {


        return ResponseEntity.ok("long");
    }

}
