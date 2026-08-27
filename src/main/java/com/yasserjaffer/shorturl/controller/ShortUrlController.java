package com.yasserjaffer.shorturl.controller;

import com.yasserjaffer.shorturl.api.ShortUrlsApi;
import com.yasserjaffer.shorturl.model.CreateShortUrlRequest;
import com.yasserjaffer.shorturl.model.CreateShortUrlResponse;
import com.yasserjaffer.shorturl.service.ShortUrlService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ShortUrlController implements ShortUrlsApi {

    private final ShortUrlService shortUrlService;

    public ShortUrlController(ShortUrlService shortUrlService) {
        this.shortUrlService = shortUrlService;
    }

    @Override
    public ResponseEntity<CreateShortUrlResponse> createShortUrl(CreateShortUrlRequest request) {
        return ResponseEntity.ok(shortUrlService.create(request));
    }

}
