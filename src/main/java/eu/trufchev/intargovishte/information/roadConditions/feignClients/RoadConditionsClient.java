package eu.trufchev.intargovishte.information.roadConditions.feignClients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "roadConditionsClient", url = "https://datasheet.api.bg/files/")
public interface RoadConditionsClient {

    @GetMapping(value = "/{date}_roadworks_r03.xml", consumes = "application/xml")
    String getRoadConditions(@PathVariable("date") String date);

}
