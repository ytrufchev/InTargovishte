package eu.trufchev.intargovishte.information.vikOutage.feignClients;

import feign.codec.Decoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

@Configuration
public class FeignConfiguration {

    @Bean
    public Decoder vikFeignDecoder() {
        return (response, type) -> {
            // Read the raw byte array from the response body
            byte[] bytes = response.body().asInputStream().readAllBytes();

            // Explicitly convert bytes to a String using Windows-1251 charset
            String decodedString = new String(bytes, Charset.forName("Windows-1251"));

            // Now, Jsoup can process the correctly decoded HTML string
            return decodedString;
        };
    }
}