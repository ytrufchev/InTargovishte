package eu.trufchev.intargovishte.information.airQuality.feignClients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "airQualityClient", url = "https://api.openweathermap.org/data/2.5")
public interface AirQualityClient {

    @GetMapping(value = "/air_pollution")
    String getAirQuality(@RequestParam(name = "lat") String lat,
                        @RequestParam(name = "lon") String lon,
                        @RequestParam(name = "appid") String key);
}