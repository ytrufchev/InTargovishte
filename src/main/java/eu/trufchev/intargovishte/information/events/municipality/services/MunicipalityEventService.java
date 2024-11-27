package eu.trufchev.intargovishte.information.events.municipality.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.trufchev.intargovishte.information.events.municipality.entities.MunicipalityEvent;
import eu.trufchev.intargovishte.information.events.municipality.feignClient.TargovishteClient;
import eu.trufchev.intargovishte.information.events.municipality.repository.MunicipalityEventRepository;
import org.jsoup.Jsoup;
import org.springframework.stereotype.Service;

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
}