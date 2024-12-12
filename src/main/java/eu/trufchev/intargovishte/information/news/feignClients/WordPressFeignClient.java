package eu.trufchev.intargovishte.information.news.feignClients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "wordpress-client", url = "https://targovishtebg.com/wp-json/wp/v2")
public interface WordPressFeignClient {

    @GetMapping("/media")
    String getMediaDetails(@RequestParam("parent") Long postId);
}
