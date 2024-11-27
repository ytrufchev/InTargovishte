package eu.trufchev.intargovishte.information.events.municipality.feignClient;

import eu.trufchev.intargovishte.FeignConfigMunicipalityOnly;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
        name = "targovishteClient",
        url = "https://targovishte.bg",
        configuration =  FeignConfigMunicipalityOnly.class
)
public interface TargovishteClient {

    @GetMapping(value = "/customSearchWCM/query", headers = {
            "Accept=*/*",
            "Accept-Encoding=gzip, deflate, br, zstd",
            "Accept-Language=en-US,en;q=0.9",
            "Connection=keep-alive",
            "Host=targovishte.bg",
            "Referer=https://targovishte.bg/wps/portal/municipality-targovishte/actual/events",
            "Sec-Ch-Ua=\"Google Chrome\";v=\"131\", \"Chromium\";v=\"131\", \"Not_A Brand\";v=\"24\"",
            "Sec-Ch-Ua-Mobile=?0",
            "Sec-Ch-Ua-Platform=\"macOS\"",
            "Sec-Fetch-Dest=empty",
            "Sec-Fetch-Mode=cors",
            "Sec-Fetch-Site=same-origin",
            "User-Agent=Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/131.0.0.0 Safari/537.36"
    })
    String getEvents(
            @RequestParam(name = "context") String context,
            @RequestParam(name = "libName") String libName,
            @RequestParam(name = "saId") String saId,
            @RequestParam(name = "atId") String atId,
            @RequestParam(name = "returnElements") String returnElements,
            @RequestParam(name = "rootPage") String rootPage,
            @RequestParam(name = "returnProperties") String returnProperties,
            @RequestParam(name = "rPP") int rPP,
            @RequestParam(name = "currentPage") int currentPage,
            @RequestParam(name = "currentUrl") String currentUrl,
            @RequestParam(name = "dateFormat") String dateFormat,
            @RequestParam(name = "ancestors") boolean ancestors,
            @RequestParam(name = "descendants") boolean descendants,
            @RequestParam(name = "orderBy") String orderBy,
            @RequestParam(name = "orderBy2") String orderBy2,
            @RequestParam(name = "orderBy3") String orderBy3,
            @RequestParam(name = "sortOrder") boolean sortOrder,
            @RequestParam(name = "searchTerm") String searchTerm,
            @RequestParam(name = "filterByProperties") String filterByProperties
    );

    // Overloaded method with default parameters for simplicity
    default String getEvents() {
        return getEvents(
                "targovishte.bg1005",
                "content",
                "98cf5967-2cf7-4bda-9bf5-f41a08cc073a",
                "cf1370df-43a1-4012-ba01-85607cf3fe92",
                "body,summary,startEventDate,endEventDate,eventType",
                "municipality-targovishte",
                "title,publishDate,generalDateOne",
                50,
                1,
                "https://targovishte.bg/wps/portal/municipality-targovishte/actual/events/",
                "dd.MM.yyyy",
                false,
                true,
                "generalDateOne",
                "publishDate",
                "title",
                false,
                "",
                "generalDateOne"
        );
    }
}
