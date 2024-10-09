package eu.trufchev.intargovishte.information.news.feignClients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "newsClient", url = "https://targovishtenews.net/wp-json/wp/v2")
public interface NewsClient {

    @GetMapping(value = "/posts")
    String getNews();
}
