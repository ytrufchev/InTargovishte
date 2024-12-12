package eu.trufchev.intargovishte.information.news.feignClients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "targovishteBgClient", url = "https://targovishtebg.com/wp-json/wp/v2")
public interface TargovishteBgClient {
    @GetMapping(value = "/posts")
    String getNews();
}
