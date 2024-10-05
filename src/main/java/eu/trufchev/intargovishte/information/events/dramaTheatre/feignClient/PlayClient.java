package eu.trufchev.intargovishte.information.events.dramaTheatre.feignClient;

import eu.trufchev.intargovishte.information.events.dramaTheatre.entities.Response;
import eu.trufchev.intargovishte.information.events.dramaTheatre.entities.ResponseEvents;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.Map;

@FeignClient(name = "PlayClient", url = "https://www.entase.com")
public interface PlayClient {

    @PostMapping(value = "/api/upcoming/productions", consumes = "application/x-www-form-urlencoded")
    Response getUpcomingEventsWithCookies(@RequestBody Map<String, ?> formParams,
                                          @RequestHeader("Cookie") String cookies);

    @PostMapping(value = "/api/embed/getevents", consumes = "application/x-www-form-urlencoded")
    ResponseEvents getProductionEvents(@RequestBody Map<String, ?> formParams,
                                       @RequestHeader("Cookie") String cookies);
}
