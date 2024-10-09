package eu.trufchev.intargovishte.information.energyOutage.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.trufchev.intargovishte.information.energyOutage.entities.EnergyOutage;
import eu.trufchev.intargovishte.information.energyOutage.repositories.EnergyOutageRepository;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EnergyOutageService {
    @Autowired
    private final EnergyOutageRepository energyOutageRepository;
    @Autowired
    private final ObjectMapper objectMapper;

    public EnergyOutageService(EnergyOutageRepository energyOutageRepository, ObjectMapper objectMapper) {
        this.energyOutageRepository = energyOutageRepository;
        this.objectMapper = new ObjectMapper();
    }

    public List<EnergyOutage> updateEnergyOutages(String jsonEvent){
        try {
            // Parse the JSON string into a JsonNode (tree model)
            JsonNode rootNode = objectMapper.readTree(jsonEvent);
            JsonNode outagesNode = rootNode.get(0).get("area_locations_for_next_48_hours");
            List<EnergyOutage> energyOutages = new ArrayList<>();
            // Iterate over the JSON array (assuming the root is an array)
            for (int i = 0; i < outagesNode.size(); i++) {
                JsonNode eventNode = outagesNode.get(i);
                // Extract fields
                String period = Jsoup.parse(eventNode.get("location_period").asText()).text();
                period.replace("\\", "");
                String text = Jsoup.parse(eventNode.get("location_text").asText()).text();
                text.replace("\\", "");

                // Create MunicipalityEvent and set fields
                EnergyOutage energyOutage = new EnergyOutage();
                energyOutage.setLocation_text(text);
                energyOutage.setLocation_period(period);

                energyOutages.add(energyOutage);
            }

            // Optionally save to repository
            energyOutageRepository.deleteAll();
            energyOutageRepository.saveAll(energyOutages);

            return energyOutages;
        } catch (Exception e) {
            throw new RuntimeException("Error parsing or saving the municipality event", e);
        }
    }
}