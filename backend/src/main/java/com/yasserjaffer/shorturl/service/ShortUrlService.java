package com.yasserjaffer.shorturl.service;

import com.yasserjaffer.shorturl.model.CreateShortUrlRequest;
import com.yasserjaffer.shorturl.model.CreateShortUrlResponse;
import com.yasserjaffer.shorturl.model.ShortUrlEntity;
import com.yasserjaffer.shorturl.repository.ShortUrlRepository;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.data.redis.core.StringRedisTemplate;
import java.time.Duration;
import java.net.URI;
import java.security.SecureRandom;
import java.util.Optional;

@Slf4j
@Service
public class ShortUrlService {
    @Value("${shorturl.base-url}")
    private String baseUrl;

    @Value("${shorturl.cache-limit}")
    private int cacheLimitHours;

    private final StringRedisTemplate redisTemplate;
    private static final String BASE62 = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final SecureRandom RANDOM = new SecureRandom();

    private final ShortUrlRepository shortUrlRepository;

    public ShortUrlService(ShortUrlRepository shortUrlRepository,
                           StringRedisTemplate redisTemplate) {
        this.shortUrlRepository = shortUrlRepository;
        this.redisTemplate = redisTemplate;
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

    public URI getOriginalUrl(String code) {

        String cachedUrl = redisTemplate.opsForValue().get(code);

        if (cachedUrl != null) {
            log.info("Cache hit for code: {}", code);
            return URI.create(cachedUrl);
        }

        log.info("Cache miss for code: {}", code);

        ShortUrlEntity entity = shortUrlRepository.findByCode(code)
                .orElseThrow(()-> new RuntimeException("Short URL not found"));

        log.info("Original URL: {}", entity.getUrl());

        redisTemplate.opsForValue().set(code, entity.getUrl(), Duration.ofHours(cacheLimitHours));
        return URI.create(entity.getUrl());
    }

    private String generateCode() {
        StringBuilder code = new StringBuilder(6);

        for (int i = 0; i < 6; i++) {
            code.append(BASE62.charAt(RANDOM.nextInt(BASE62.length())));
        }
        return code.toString();
    }
}
