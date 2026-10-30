package com.artur.slup.config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Configuration
public class DatabaseConfig {

    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    @Bean
    public Connection connection() throws SQLException {

        String dbUrl = url != null ? url : System.getenv("spring.datasource.url");
        String dbUser = username != null ? username : System.getenv("spring.datasource.username");
        String dbPass = password != null ? password : System.getenv("spring.datasource.password");

        return DriverManager.getConnection(dbUrl, dbUser, dbPass);

    }
}
