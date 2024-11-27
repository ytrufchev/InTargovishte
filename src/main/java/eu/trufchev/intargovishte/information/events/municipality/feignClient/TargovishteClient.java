package eu.trufchev.intargovishte.information.events.municipality.feignClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(
        name = "targovishteClient",
        url = "https://targovishte.bg"
)
public interface TargovishteClient {

    @GetMapping(value = "/customSearchWCM/query?context=targovishte.bg1005&libName=content&saId=98cf5967-2cf7-4bda-9bf5-f41a08cc073a&atId=cf1370df-43a1-4012-ba01-85607cf3fe92&returnElements=body,summary,startEventDate,endEventDate,eventType&rootPage=municipality-targovishte&returnProperties=title,publishDate,generalDateOne&rPP=50&currentPage=1&currentUrl=https%3A%2F%2Ftargovishte.bg%2Fwps%2Fportal%2Fmunicipality-targovishte%2Factual%2Fevents%2F&dateFormat=dd.MM.yyyy&ancestors=false&descendants=true&orderBy=generalDateOne&orderBy2=publishDate&orderBy3=title&sortOrder=false&searchTerm=&filterByProperties=generalDateOne")
    String getEvents();
}
