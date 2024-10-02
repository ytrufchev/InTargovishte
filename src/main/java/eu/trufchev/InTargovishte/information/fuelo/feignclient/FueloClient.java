package eu.trufchev.InTargovishte.information.fuelo.feignclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "fueloClient", url = "https://fuelo.net/ajax/get_infowindow_content")
public interface FueloClient {

    @GetMapping(value = "/{id}", consumes = "application/x-www-form-urlencoded")
    String getGasstationDetails(@PathVariable("id") String id, @RequestParam("lang") String lang);
}