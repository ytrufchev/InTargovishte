package eu.trufchev.intargovishte.information.events.municipality.feignClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "municipalityEventClient", url = "https://targovishte.bg/wps/portal/municipality-targovishte")
public interface MunicipalityEventClient {

    @GetMapping(value = "/actual/events")
    String getMunicipalityEvents();

}