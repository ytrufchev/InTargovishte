package eu.trufchev.intargovishte.information.airQuality.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import eu.trufchev.intargovishte.information.airQuality.entities.AirQuality;
import eu.trufchev.intargovishte.information.airQuality.repositories.AirQualityRepository;
import eu.trufchev.intargovishte.information.airQuality.services.AirQualityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/information/air")
public class AirQualityController {
    @Autowired
    AirQualityService airQualityService;
    @Autowired
    AirQualityRepository airQualityRepository;

    @GetMapping("/update")
    public ResponseEntity<List<AirQuality>> updateAirQuality() throws JsonProcessingException {
        List<AirQuality> airQuality = new ArrayList<>();
        airQuality.add(airQualityService.getAirQuality());
        airQualityRepository.deleteAll();
        airQualityRepository.saveAll(airQuality);
        return ResponseEntity.ok(airQuality);
    }
    @GetMapping("/all")
    public ResponseEntity<List<AirQuality>> getAirQuality(){
        List<AirQuality> airQuality = new ArrayList<>();
        airQualityRepository.findAll().forEach(airQuality::add);
        return ResponseEntity.ok(airQuality);
    }
}