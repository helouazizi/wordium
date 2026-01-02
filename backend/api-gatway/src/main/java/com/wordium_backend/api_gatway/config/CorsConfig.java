package com.wordium_backend.api_gatway.config;

import org.springframework.context.annotation.Configuration;

@Configuration
public class CorsConfig {

    // @Bean
    // public CorsWebFilter corsFilter() {
    //     CorsConfiguration config = new CorsConfiguration();
    //     config.setAllowCredentials(true); // allow cookies / credentials
    //     config.addAllowedOrigin("*"); // frontend URL
    //     config.addAllowedHeader("*"); // allow all headers
    //     config.addAllowedMethod("*"); // allow GET, POST, PUT, DELETE, OPTIONS

    //     UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    //     source.registerCorsConfiguration("/**", config); // apply to all paths

    //     return new CorsWebFilter(source);
    // }
}


