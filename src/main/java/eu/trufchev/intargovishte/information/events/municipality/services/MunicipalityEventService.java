package eu.trufchev.intargovishte.information.events.municipality.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.trufchev.intargovishte.information.events.municipality.entities.MunicipalityEvent;
import eu.trufchev.intargovishte.information.events.municipality.feignClient.TargovishteClient;
import eu.trufchev.intargovishte.information.events.municipality.repository.MunicipalityEventRepository;
import org.jsoup.Jsoup;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
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
        return CurlEvent();
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

                if (i == 1) {
                    System.out.println("First Event - Parsed Body Length: " + body.length()); // Log length of parsed text for the first event
                }

                if(body.length() > 4999) {
                    body = body.substring(0, 4999); // Truncate to 4999 characters
                }

                // Create MunicipalityEvent and set fields
                MunicipalityEvent municipalityEvent = new MunicipalityEvent();
                municipalityEvent.setDate(startEventDate);
                municipalityEvent.setTitle(title);
                municipalityEvent.setDescription(body);

                System.out.println("First Event - Final Description Length: " + (municipalityEvent.getDescription() != null ? municipalityEvent.getDescription().length() : 0));
                System.out.println("First Event - Description to be saved: " + municipalityEvent.getDescription());

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
        String url = "https://targovishte.bg/customSearchWCM/query?context=targovishte.bg1005&libName=content&saId=98cf5967-2cf7-4bda-9bf5-f41a08cc073a&atId=cf1370df-43a1-4012-ba01-85607cf3fe92&returnElements=body,summary,startEventDate,endEventDate,eventType&filterByElements=&rootPage=municipality-targovishte&returnProperties=title,publishDate,generalDateOne&rPP=50&currentPage=1&currentUrl=https%3A%2F%2Ftargovishte.bg%2Fwps%2Fportal%2Fmunicipality-targovishte%2Factual%2Fevents%2F&dateFormat=dd.MM.yyyy&ancestors=false&descendants=true&orderBy=generalDateOne&orderBy2=publishDate&orderBy3=title&sortOrder=false&searchTerm=&from=&before=&filterByProperties=generalDateOne&catId=";
        try {
            // Create URL object
            URL obj = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) obj.openConnection();

            // Set HTTP method
            connection.setRequestMethod("GET");

            // Set headers
            connection.setRequestProperty("Accept", "*/*");
            connection.setRequestProperty("Accept-Encoding", "gzip, deflate, br, zstd");
            connection.setRequestProperty("Accept-Language", "en-US,en;q=0.9");
            connection.setRequestProperty("Connection", "keep-alive");
            connection.setRequestProperty("Host", "targovishte.bg");
            connection.setRequestProperty("Referer", "https://targovishte.bg/wps/portal/municipality-targovishte/actual/events");
            connection.setRequestProperty("Sec-Ch-Ua", "\"Google Chrome\";v=\"131\", \"Chromium\";v=\"131\", \"Not_A Brand\";v=\"24\"");
            connection.setRequestProperty("Sec-Ch-Ua-Mobile", "?0");
            connection.setRequestProperty("Sec-Ch-Ua-Platform", "\"macOS\"");
            connection.setRequestProperty("Sec-Fetch-Dest", "empty");
            connection.setRequestProperty("Sec-Fetch-Mode", "cors");
            connection.setRequestProperty("Sec-Fetch-Site", "same-origin");
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/131.0.0.0 Safari/537.36");
            connection.setRequestProperty("Postman-Token", "3e68808c-cdb6-4a9b-89c2-d361c7064189");

            // Get the response code
            int responseCode = connection.getResponseCode();
            System.out.println("Response Code: " + responseCode);

            // Read response
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // Print the response
            String responseString = response.toString();
            System.out.println("Response: " + responseString);
    return responseString;
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }
}