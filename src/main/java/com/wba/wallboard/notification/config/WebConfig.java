package com.wba.wallboard.notification.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private Environment env;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // Read CORS_ALLOWED_ORIGINS from environment, default to localhost:4200
        String allowedOrigins = env.getProperty("CORS_ALLOWED_ORIGINS",
                "http://localhost:4200");

        // Split multiple origins by comma
        String[] origins = allowedOrigins.split(",");

        registry.addMapping("/api/**")
                .allowedOriginPatterns(origins) // allows wildcards if needed
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}
