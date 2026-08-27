package com.yasserjaffer.shorturl.service;

import com.yasserjaffer.shorturl.model.CreateShortUrlRequest;
import com.yasserjaffer.shorturl.model.CreateShortUrlResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ShortUrlService {

    public CreateShortUrlResponse create(CreateShortUrlRequest request) {
        log.info("Request URL: {}", request.getUrl());


        return new CreateShortUrlResponse()
                .shortUrl(request.getUrl());
    }

}
