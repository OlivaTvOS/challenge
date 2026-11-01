package com.artur.slup.config;

import org.springframework.context.annotation.Configuration;

/**
 * A conexao JDBC nao fica mais registrada como um bean Connection singleton.
 * O Spring Boot cria o DataSource a partir de spring.datasource.* e os DAOs
 * abrem uma conexao do pool para cada operacao.
 */
@Configuration
public class DatabaseConfig {
}
