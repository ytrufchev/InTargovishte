package eu.trufchev.intargovishte.information.news.feignClients;

import feign.RequestLine;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "wordpress-client", url = "https://targovishtebg.com/wp-json/wp/v2")
public interface WordPressFeignClient {

    @RequestLine("GET")
    String getFromUrl(String url);
}
