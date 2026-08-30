package com.yasserjaffer.shorturl.exception;

public class ShortUrlNotFoundException extends RuntimeException {

    public ShortUrlNotFoundException() {
        super("Short URL not found");
    }
}
