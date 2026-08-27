package com.yasserjaffer.shorturl.repository;


import com.yasserjaffer.shorturl.model.ShortUrlEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShortUrlRepository extends JpaRepository<ShortUrlEntity, Long> {
}
