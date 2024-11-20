package eu.trufchev.intargovishte.information.events.municipality.feignClient;

import eu.trufchev.intargovishte.FeignConfigMunicipalityOnly;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "targovishteClient", url = "https://targovishte.bg", configuration = FeignConfigMunicipalityOnly.class)
public interface TargovishteClient {

    @GetMapping("/customSearchWCM/query")
    String getMunicipalityEvents(@RequestHeader("Accept") String accept,
                                 @RequestHeader("Accept-Encoding") String acceptEncoding,
                                 @RequestHeader("Accept-Language") String acceptLanguage,
                                 @RequestHeader("Connection") String connection,
                                 @RequestHeader("Host") String host,
                                 @RequestHeader("Referer") String referer,
                                 @RequestHeader("Sec-Ch-Ua") String secChUa,
                                 @RequestHeader("Sec-Ch-Ua-Mobile") String secChUaMobile,
                                 @RequestHeader("Sec-Ch-Ua-Platform") String secChUaPlatform,
                                 @RequestHeader("Sec-Fetch-Dest") String secFetchDest,
                                 @RequestHeader("Sec-Fetch-Mode") String secFetchMode,
                                 @RequestHeader("Sec-Fetch-Site") String secFetchSite,
                                 @RequestHeader("User-Agent") String userAgent,
                                 @RequestParam("context") String context,
                                 @RequestParam("libName") String libName,
                                 @RequestParam("saId") String saId,
                                 @RequestParam("atId") String atId,
                                 @RequestParam("returnElements") List<String> returnElements,
                                 @RequestParam("rootPage") String rootPage,
                                 @RequestParam("returnProperties") List<String> returnProperties,
                                 @RequestParam("rPP") int rPP,
                                 @RequestParam("currentPage") int currentPage,
                                 @RequestParam("currentUrl") String currentUrl,
                                 @RequestParam("dateFormat") String dateFormat,
                                 @RequestParam("ancestors") boolean ancestors,
                                 @RequestParam("descendants") boolean descendants,
                                 @RequestParam("orderBy") String orderBy,
                                 @RequestParam("orderBy2") String orderBy2,
                                 @RequestParam("orderBy3") String orderBy3,
                                 @RequestParam("sortOrder") boolean sortOrder,
                                 @RequestParam("searchTerm") String searchTerm,
                                 @RequestParam("from") String from,
                                 @RequestParam("before") String before,
                                 @RequestParam("filterByProperties") String filterByProperties,
                                 @RequestParam("catId") String catId);
}
