package com.yasserjaffer.shorturl.scheduler;

import com.yasserjaffer.shorturl.repository.ShortUrlRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;

@Slf4j
@Transactional
@Component
public class ShortUrlCleanupScheduler {

    private final ShortUrlRepository shortUrlRepository;

    public ShortUrlCleanupScheduler(ShortUrlRepository shortUrlRepository) {
        this.shortUrlRepository = shortUrlRepository;
    }

    @Scheduled(cron = "${shorturl.cleanup-cron}")
    public void cleanupExpiredUrls() {
        long deleted = shortUrlRepository.deleteByExpiresAtBefore(LocalDateTime.now());
        log.info("Expired short URL cleanup completed. Deleted {} records", deleted);
    }
}
