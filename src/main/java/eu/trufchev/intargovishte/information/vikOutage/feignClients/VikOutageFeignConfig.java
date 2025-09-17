package eu.trufchev.intargovishte.information.vikOutage.feignClients;

import feign.codec.Decoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.nio.charset.Charset;

// This class is for VikOutageClient ONLY
@Configuration
public class VikOutageFeignConfig {

    @Bean
    public Decoder vikOutageDecoder() {
        return (response, type) -> {
            byte[] bytes = response.body().asInputStream().readAllBytes();
            return new String(bytes, Charset.forName("Windows-1251"));
        };
    }
}