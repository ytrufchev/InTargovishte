package eu.trufchev.intargovishte.information.energyOutage.feignClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.Map;

@FeignClient(name = "energoProClient", url = "https://www.energo-pro.bg/bg/profil/xhr/", configuration = EnergoProFeignClientConfig.class)
public interface EnergoProClient {

    @GetMapping
    String getInterruptions(
            @RequestParam("method") String method,
            @RequestParam("region_id") int regionId,
            @RequestParam("type") String type,
            @RequestParam("offset") int offset,
            @RequestParam("archive_from_date") String fromDate,
            @RequestParam("archive_to_date") String toDate,
            @RequestHeader("User-Agent") String userAgent,
            @RequestHeader("Accept") String accept,
            @RequestHeader("X-Requested-With") String xRequestedWith,
            @RequestHeader("Referer") String referer
    );
}
