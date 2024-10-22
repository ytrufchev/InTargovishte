package eu.trufchev.intargovishte.config;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
    @Value("${app.jwt-refresh-expiration-in-ms}")
    private long refreshTokenDurationMs;

    @Bean
    public long refreshTokenDurationMs() {
        return refreshTokenDurationMs;
    }
}