package eu.trufchev.intargovishte.information.events.dramaTheatre.feignClient;

import eu.trufchev.intargovishte.FeignConfig;
import feign.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "CookieClient", url = "https://entase.com", configuration = FeignConfig.class)
public interface CookieClient {

    @GetMapping(value = "/?city={city}")
    Response getCityPage(@PathVariable("city") String city);

}
