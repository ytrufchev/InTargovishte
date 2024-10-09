package eu.trufchev.intargovishte.information.weather.feignClients;

import eu.trufchev.intargovishte.information.weather.entities.WeatherResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "weatherClient", url = "https://weather.visualcrossing.com/VisualCrossingWebServices/rest/services/timeline/")
public interface WeatherClient {

    @GetMapping(value = "/targovishte")
    WeatherResponse getCurrentConditions(@RequestParam("unitGroup") String unitGroup,
                                         @RequestParam("include") String include,
                                         @RequestParam("key") String key,
                                         @RequestParam("contentType") String contentType
    );

}
