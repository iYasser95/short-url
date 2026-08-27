package com.yasserjaffer.shorturl.service;

import com.yasserjaffer.shorturl.model.CreateShortUrlRequest;
import com.yasserjaffer.shorturl.model.CreateShortUrlResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.Base64;

@Slf4j
@Service
public class ShortUrlService {

    public CreateShortUrlResponse create(CreateShortUrlRequest request) {
        log.info("Request URL: {}", request.getUrl());

        String url = request.getUrl().toString();
        String base64 = Base64.getEncoder().encodeToString(url.getBytes());
        int middle = (base64.length() - 6) / 2; // This is used to get the middle index of the base64

        String shortUrl = base64.substring(middle,middle + 6); // Get the shortURL from the middle of base64
        URI response = URI.create(shortUrl);

        log.info("Full Base64: {}", base64);
        log.info("Middle index: {}", middle);
        log.info("Short String: {} | Length: {}", shortUrl, shortUrl.length());
        return new CreateShortUrlResponse()
                .shortUrl(response);
    }

}
