package com.artur.slup.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {

    @Value("${app.cors.allowed-origins:http://localhost:5173}")
    private String allowedOrigins;

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration configuracao = new CorsConfiguration();
        String[] origens = allowedOrigins.split(",");
        for (int i = 0; i < origens.length; i++) {
            origens[i] = origens[i].trim();
        }

        configuracao.setAllowedOrigins(Arrays.asList(origens));
        configuracao.setAllowedMethods(Arrays.asList(
                "GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuracao.setAllowedHeaders(Arrays.asList("*"));
        configuracao.setAllowCredentials(false);

        UrlBasedCorsConfigurationSource origem = new UrlBasedCorsConfigurationSource();
        origem.registerCorsConfiguration("/**", configuracao);
        return new CorsFilter(origem);
    }
}
