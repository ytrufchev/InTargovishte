package eu.trufchev.intargovishte.information.news.feignClients;

import feign.RequestLine;
import feign.Param;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "wordpress-client", url = "") // Base URL not needed here for dynamic URLs
public interface WordPressFeignClient {

    // Allows passing a complete dynamic URL
    @RequestLine("GET {url}")
    String getFromUrl(@Param("url") String url);
}
