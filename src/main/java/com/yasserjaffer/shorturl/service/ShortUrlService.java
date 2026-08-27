package com.yasserjaffer.shorturl.service;

import com.yasserjaffer.shorturl.model.CreateShortUrlRequest;
import com.yasserjaffer.shorturl.model.CreateShortUrlResponse;
import com.yasserjaffer.shorturl.model.ShortUrlEntity;
import com.yasserjaffer.shorturl.repository.ShortUrlRepository;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.security.SecureRandom;
import java.util.Optional;

@Slf4j
@Service
public class ShortUrlService {
    @Value("${shorturl.base-url}")
    private String baseUrl;

    private static final String BASE62 = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final SecureRandom RANDOM = new SecureRandom();

    private final ShortUrlRepository shortUrlRepository;

    public ShortUrlService(ShortUrlRepository shortUrlRepository) {
        this.shortUrlRepository = shortUrlRepository;
    }

    public CreateShortUrlResponse create(CreateShortUrlRequest request) {
        String url = request.getUrl().toString();
        log.info("Request URL: {}", url);

        // Check if URL already exists in DB and return it.
        Optional<ShortUrlEntity> existingUrl = shortUrlRepository.findByUrl(url);
        if (existingUrl.isPresent()) {
            log.info("URL already exists: {}", existingUrl.toString());
            return new CreateShortUrlResponse()
                    .shortUrl(URI.create(baseUrl + "/" + existingUrl.get().getCode()));
        }

        // Generate Base62 code
        String code;
        do {
            code = generateCode();
        } while (shortUrlRepository.existsByCode(code));

        log.info("Generate Code: {}", code);

        // Save the code and URL in the DB.
        ShortUrlEntity entity = new ShortUrlEntity();
        entity.setCode(code);
        entity.setUrl(url);
        shortUrlRepository.save(entity);

        return new CreateShortUrlResponse()
                .shortUrl(URI.create(baseUrl + "/" + code));

    }

    private String generateCode() {
        StringBuilder code = new StringBuilder(6);

        for (int i = 0; i < 6; i++) {
            code.append(BASE62.charAt(RANDOM.nextInt(BASE62.length())));
        }
        return code.toString();
    }
}
