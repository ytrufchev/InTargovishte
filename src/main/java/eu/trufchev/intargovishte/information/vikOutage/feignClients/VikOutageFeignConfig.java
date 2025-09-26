package eu.trufchev.intargovishte.information.vikOutage.feignClients;

import feign.codec.Decoder;
import feign.optionals.OptionalDecoder;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.SpringDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.stream.Collectors;

@Configuration
public class VikOutageFeignConfig {

    @Bean
    public Decoder customDecoder() {
        // Correctly build a SpringDecoder with HttpMessageConverters
        SpringDecoder defaultDecoder = new SpringDecoder(() -> new HttpMessageConverters());

        return (response, type) -> {
            // For plain String responses, decode manually using Windows-1251
            if (type.equals(String.class)) {
                try (BufferedReader reader = new BufferedReader(
                        new InputStreamReader(response.body().asInputStream(), Charset.forName("windows-1251")))) {
                    return reader.lines().collect(Collectors.joining("\n"));
                }
            }
            // Otherwise fallback to Spring's default decoder
            return new OptionalDecoder(defaultDecoder).decode(response, type);
        };
    }
}
