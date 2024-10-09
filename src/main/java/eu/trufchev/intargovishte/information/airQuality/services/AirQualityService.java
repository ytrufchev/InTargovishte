package eu.trufchev.intargovishte.information.airQuality.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.trufchev.intargovishte.information.airQuality.entities.*;
import eu.trufchev.intargovishte.information.airQuality.feignClients.AirQualityClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AirQualityService {
    @Autowired
    AirQualityClient airQualityClient;
    @Autowired
    ObjectMapper objectMapper;
    public AirQuality getAirQuality() throws JsonProcessingException {
        final String lat = "43.15";
        final String lon = "26.34";
        final String key = "540aef93c732bdc81e74cbfa5ea31132";
        String airQualityResponse = airQualityClient.getAirQuality(lat, lon, key);
        JsonNode airQualityJson = objectMapper.readTree(airQualityResponse);
            AirQuality airQuality = new AirQuality();
            Coord coord = new Coord();
            List list = new List();
            Component component = new Component();
            Main main = new Main();
            coord.setLat(airQualityJson.get("coord").get("lat").doubleValue());
            coord.setLon(airQualityJson.get("coord").get("lon").doubleValue());
            main.setAqi(airQualityJson.get("list").get(0).get("main").get("aqi").asText());
            list.setDt(airQualityJson.get("list").get(0).get("dt").longValue());
            component.setCo(airQualityJson.get("list").get(0).get("components").get("co").doubleValue());
            component.setNo(airQualityJson.get("list").get(0).get("components").get("no").doubleValue());
            component.setNo2(airQualityJson.get("list").get(0).get("components").get("no2").doubleValue());
            component.setO3(airQualityJson.get("list").get(0).get("components").get("o3").doubleValue());
            component.setSo2(airQualityJson.get("list").get(0).get("components").get("so2").doubleValue());
            component.setPm2_5(airQualityJson.get("list").get(0).get("components").get("pm2_5").doubleValue());
            component.setPm10(airQualityJson.get("list").get(0).get("components").get("pm10").doubleValue());
            component.setNh3(airQualityJson.get("list").get(0).get("components").get("nh3").doubleValue());

            list.setMain(main);
            list.setComponents(component);

            airQuality.setCoord(coord);
            airQuality.setList(list);

            return airQuality;
    }
}
