package com.wordium_backend.api_gatway;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FallbackController {

    @GetMapping("/fallback")
    public String fallback() {
        return "Service is temporarily unavailable. Please try again later.";
    }
}