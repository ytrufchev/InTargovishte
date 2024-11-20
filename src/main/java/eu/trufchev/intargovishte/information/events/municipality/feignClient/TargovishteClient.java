package eu.trufchev.intargovishte.information.events.municipality.feignClient;

import eu.trufchev.intargovishte.FeignConfigMunicipalityOnly;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "targovishteClient", url = "https://targovishte.bg", configuration = FeignConfigMunicipalityOnly.class)
public interface TargovishteClient {

    @GetMapping("/customSearchWCM/query?context=targovishte.bg1005&libName=content&saId=98cf5967-2cf7-4bda-9bf5-f41a08cc073a&atId=cf1370df-43a1-4012-ba01-85607cf3fe92&returnElements=body,summary,startEventDate,endEventDate,eventType&filterByElements=&rootPage=municipality-targovishte&returnProperties=title,publishDate,generalDateOne&rPP=50&currentPage=1&currentUrl=https%3A%2F%2Ftargovishte.bg%2Fwps%2Fportal%2Fmunicipality-targovishte%2Factual%2Fevents%2F&dateFormat=dd.MM.yyyy&ancestors=false&descendants=true&orderBy=generalDateOne&orderBy2=publishDate&orderBy3=title&sortOrder=false&searchTerm=&from=&before=&filterByProperties=generalDateOne&catId=")
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
                                 @RequestHeader("User-Agent") String userAgent);
}
