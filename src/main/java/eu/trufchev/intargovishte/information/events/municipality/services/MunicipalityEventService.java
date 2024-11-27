package eu.trufchev.intargovishte.information.events.municipality.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.trufchev.intargovishte.information.events.municipality.entities.MunicipalityEvent;
import eu.trufchev.intargovishte.information.events.municipality.feignClient.TargovishteClient;
import eu.trufchev.intargovishte.information.events.municipality.repository.MunicipalityEventRepository;
import org.jsoup.Jsoup;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class MunicipalityEventService {

    private final MunicipalityEventRepository municipalityEventRepository;
    private final ObjectMapper objectMapper;
    private final TargovishteClient targovishteClient;

    public MunicipalityEventService(MunicipalityEventRepository municipalityEventRepository, TargovishteClient targovishteClient) {
        this.municipalityEventRepository = municipalityEventRepository;
        this.targovishteClient = targovishteClient;
        this.objectMapper = new ObjectMapper();
    }

    public String fetchEvents() {
        return targovishteClient.getEvents();
    }

    public List<MunicipalityEvent> parseAndSave() {
        try {
            String data = fetchEvents();
            JsonNode rootNode = objectMapper.readTree(data);
            List<MunicipalityEvent> municipalityEvents = new ArrayList<>();
            // Iterate over the JSON array (assuming the root is an array)
            for (int i = 1; i < rootNode.size(); i++) {
                JsonNode eventNode = rootNode.get(i);

                // Extract fields
                String startEventDate = eventNode.get("contentstartEventDate").get("startEventDate").asText();
                String title = eventNode.get("contentTitle").get("title").asText();
                String bodyHtml = eventNode.get("contentbody").get("body").asText();
                String body = Jsoup.parse(bodyHtml).text(); // Remove HTML

                // Create MunicipalityEvent and set fields
                MunicipalityEvent municipalityEvent = new MunicipalityEvent();
                municipalityEvent.setDate(startEventDate);
                municipalityEvent.setTitle(title);
                municipalityEvent.setDescription(body);

                municipalityEvents.add(municipalityEvent);
            }

            // Optionally save to repository
            municipalityEventRepository.deleteAll();
            municipalityEventRepository.saveAll(municipalityEvents);

            return municipalityEvents;
        } catch (Exception e) {
            throw new RuntimeException("Error parsing or saving the municipality event", e);
        }
    }

    public String CurlEvent() {
        try {
            // Create HttpClient instance
            HttpClient client = HttpClient.newHttpClient();

            // Build the request using curl headers and URL
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("https://targovishte.bg/customSearchWCM/query" +
                            "?context=targovishte.bg1005" +
                            "&libName=content" +
                            "&saId=98cf5967-2cf7-4bda-9bf5-f41a08cc073a" +
                            "&atId=cf1370df-43a1-4012-ba01-85607cf3fe92" +
                            "&returnElements=body,summary,startEventDate,endEventDate,eventType" +
                            "&filterByElements=&rootPage=municipality-targovishte" +
                            "&returnProperties=title,publishDate,generalDateOne" +
                            "&rPP=50&currentPage=1" +
                            "&currentUrl=https%3A%2F%2Ftargovishte.bg%2Fwps%2Fportal%2Fmunicipality-targovishte%2Factual%2Fevents%2F" +
                            "&dateFormat=dd.MM.yyyy&ancestors=false&descendants=true" +
                            "&orderBy=generalDateOne&orderBy2=publishDate&orderBy3=title" +
                            "&sortOrder=false&searchTerm=&from=&before=&filterByProperties=generalDateOne&catId="))
                    .header("Accept", "*/*")
                    .header("Accept-Encoding", "gzip, deflate, br, zstd")
                    .header("Accept-Language", "en-US,en;q=0.9")
                    .header("Connection", "keep-alive")
                    .header("Host", "targovishte.bg")
                    .header("Referer", "https://targovishte.bg/wps/portal/municipality-targovishte/actual/events")
                    .header("Sec-Ch-Ua", "\"Google Chrome\";v=\"131\", \"Chromium\";v=\"131\", \"Not_A Brand\";v=\"24\"")
                    .header("Sec-Ch-Ua-Mobile", "?0")
                    .header("Sec-Ch-Ua-Platform", "\"macOS\"")
                    .header("Sec-Fetch-Dest", "empty")
                    .header("Sec-Fetch-Mode", "cors")
                    .header("Sec-Fetch-Site", "same-origin")
                    .header("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/131.0.0.0 Safari/537.36")
                    .header("Postman-Token", "3e68808c-cdb6-4a9b-89c2-d361c7064189")
                    .GET()
                    .build();

            // Send the request
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Print the response status and body
            System.out.println("Response Code: " + response.statusCode());
            System.out.println("Response Body: " + response.body());
            return response.body();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "error";
    }
}