package eu.trufchev.intargovishte.information.events.puppetTheatre.feignClients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "puppetTheaterClient", url = "https://puppettargovishte.org/")
public interface PuppetTheaterClient {

    @GetMapping(value = "/%d0%bf%d1%80%d0%be%d0%b3%d1%80%d0%b0%d0%bc%d0%b0")
    String getPuppetShows();

}
