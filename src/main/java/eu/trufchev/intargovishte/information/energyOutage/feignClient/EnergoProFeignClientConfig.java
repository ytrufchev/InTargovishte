package eu.trufchev.intargovishte.information.energyOutage.feignClient;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EnergoProFeignClientConfig {

    @Bean
    public RequestInterceptor energoProRequestInterceptor() {
        return new RequestInterceptor() {
            @Override
            public void apply(RequestTemplate template) {
                template.header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/108.0.0.0 Safari/537.36");
                template.header("Accept", "application/json, text/javascript, */*; q=0.01");
                template.header("X-Requested-With", "XMLHttpRequest");
                template.header("Referer", "https://www.energo-pro.bg/bg/za-klienta/avariini-remontni-raboti-i-planirani-prekusvaniya");
            }
        };
    }
}