package eu.trufchev.intargovishte.information.energyOutage.feignClient;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import feign.codec.Decoder;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.SpringDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

@Configuration
public class EnergoProFeignConfig {

    @Bean
    public Decoder energoProDecoder() {
        // Use Spring's decoder which handles JSON automatically
        HttpMessageConverter jacksonConverter = new MappingJackson2HttpMessageConverter();
        ObjectFactory<HttpMessageConverters> messageConverters = () -> new HttpMessageConverters(jacksonConverter);
        return new SpringDecoder(messageConverters);
    }

    @Bean
    public RequestInterceptor energoProRequestInterceptor() {
        return new RequestInterceptor() {
            @Override
            public void apply(RequestTemplate template) {
                // Add required headers for EnergoProClient
                template.header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/108.0.0.0 Safari/537.36");
                template.header("Accept", "application/json, text/javascript, */*; q=0.01");
                template.header("X-Requested-With", "XMLHttpRequest");
                template.header("Referer", "https://www.energo-pro.bg/bg/za-klienta/avariini-remontni-raboti-i-planirani-prekusvaniya");
            }
        };
    }
}