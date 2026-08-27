package com.yasserjaffer.shorturl.repository;


import com.yasserjaffer.shorturl.model.ShortUrlEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ShortUrlRepository extends JpaRepository<ShortUrlEntity, Long> {
    Optional<ShortUrlEntity> findByUrl(String url);
    boolean existsByCode(String code);
}
