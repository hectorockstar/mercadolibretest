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

    @PostMapping("/create-short-url")
    @ResponseBody
    public ResponseEntity<Object> createShortUrl(@RequestParam("url") String url) {
        return ResponseEntity.ok(Utils.toJSONFromObject(urlManegementService.createShortUrl(url)));
    }



    @GetMapping("/get-long-url")
    @ResponseBody
    public ResponseEntity<Object> getLongUrl() {


        return ResponseEntity.ok("long");
    }

}
