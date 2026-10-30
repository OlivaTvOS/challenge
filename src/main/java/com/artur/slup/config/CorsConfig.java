package com.artur.slup.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    public void addCorsMappings(CorsRegistry registry){

        registry.addMapping("/**") // Libera os Endpoints
                .allowedOrigins("http://localhost:5173")// Url onde o react tá rodando
                .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE") // Métodos permitidos
                .allowedHeaders("*"); // Todos os Headers
    }
}
