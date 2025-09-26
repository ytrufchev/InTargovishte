package eu.trufchev.intargovishte.information.vikOutage.feignClients;

import feign.codec.Decoder;
import org.springframework.context.annotation.Bean;
import java.nio.charset.Charset;
import java.io.IOException;

public class VikOutageFeignConfig {

    @Bean
    public Decoder customDecoder() {
        return (response, type) -> {
            // **Crucial: Only apply to String responses.**
            if (type.equals(String.class)) {
                try {
                    // 1. Read the raw bytes from the response body.
                    // This avoids any auto-decoding by standard Spring/Feign decoders.
                    byte[] bytes = response.body().asInputStream().readAllBytes();

                    // 2. Explicitly construct the String using the correct Windows-1251 charset.
                    return new String(bytes, Charset.forName("Windows-1251"));

                } catch (IOException e) {
                    throw new RuntimeException("Failed to decode response with Windows-1251", e);
                }
            }

            // For all other types (DTOs, etc.), throw an exception
            // since this client is only expected to return a String (HTML).
            throw new UnsupportedOperationException(
                    "Custom VikOutageDecoder only supports String response type."
            );
        };
    }
}