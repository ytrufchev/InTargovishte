package eu.trufchev.intargovishte.information.events.dramaTheatre.service;

import eu.trufchev.intargovishte.information.events.dramaTheatre.entities.*;
import eu.trufchev.intargovishte.information.events.dramaTheatre.feignClient.CookieClient;
import eu.trufchev.intargovishte.information.events.dramaTheatre.feignClient.PlayClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class GetPlaysResponse {

    @Autowired
    private CookieClient cookieClient;
    @Autowired
    private PlayClient playClient;

    public List<Play> getPlaysForTargovishte() {
        // Fetch cookies once and reuse them
        String cookieHeader = fetchCookiesForCity("%D0%A2%D1%8A%D1%80%D0%B3%D0%BE%D0%B2%D0%B8%D1%89%D0%B5");
        System.out.println("Trying to get plays");
        if (cookieHeader == null) {
            // Handle case where cookies are not available
            return Collections.emptyList();
        }

        // Prepare form parameters
        Map<String, Object> formParams = new HashMap<>();
        formParams.put("upcoming", "true");
        formParams.put("nearest", "true");
        formParams.put("city", "Търговище");
        formParams.put("search", "");
        formParams.put("skip", 0);
        formParams.put("limit", 20);

        // Fetch upcoming events
        Response response1 = playClient.getUpcomingEventsWithCookies(formParams, cookieHeader);
        System.out.println(response1.toString());
        if (response1 == null || response1.getProductions() == null) {
            System.out.println("No plays found");
            return Collections.emptyList();  // Handle API failure
        }

        List<Production> productions = response1.getProductions();
        System.out.println("Plays found and added to list");
        // Collect events for each production
        String prod = "Production:";
        List<Event> events = productions.stream()
                .map(production -> getEventsForTargovishte(prod+production.getId(), cookieHeader))
                .flatMap(List::stream)  // Flatten the list of lists
                .collect(Collectors.toList());

        // Convert to Play list
        ResponseToPlayList responseToPlayList = new ResponseToPlayList();
        System.out.println("Returning plays list");
        return responseToPlayList.playsResponseToPlays(productions, events);
    }

    public List<Event> getEventsForTargovishte(String productionId, String cookieHeader) {
        System.out.println("Trying to get targovishte plays");
        // Prepare form parameters for fetching events
        Map<String, Object> formParams2 = new HashMap<>();
        formParams2.put("obj", productionId);
        formParams2.put("skip", 0);
        formParams2.put("limit", 20);
        formParams2.put("city", "Търговище");

        ResponseEvents responseEvents = playClient.getProductionEvents(formParams2, cookieHeader);
        return (responseEvents != null && responseEvents.getEvents() != null)
                ? responseEvents.getEvents()
                : Collections.emptyList();  // Handle empty response or nulls
    }

    // Utility method to fetch cookies
    private String fetchCookiesForCity(String cityEncoded) {
        try {
            System.out.println("Trying to get cookies");
            feign.Response response = cookieClient.getCityPage(cityEncoded);
            List<String> rawCookies = new ArrayList<>(response.headers().getOrDefault("Set-Cookie", Collections.emptyList()));

            // Clean and format cookies
            List<String> cookies = rawCookies.stream()
                    .map(cookie -> cookie.split(";", 2)[0])  // Split by semicolon and take key=value part
                    .collect(Collectors.toList());

            return String.join("; ", cookies);  // Join cookies into a single header
        } catch (Exception e) {
            // Handle exceptions (e.g., network issues)
            e.printStackTrace();
            return null;
        }
    }
}
