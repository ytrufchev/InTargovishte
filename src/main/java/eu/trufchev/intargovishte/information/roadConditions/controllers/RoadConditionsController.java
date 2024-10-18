package eu.trufchev.intargovishte.information.roadConditions.controllers;

import eu.trufchev.intargovishte.information.roadConditions.entities.RoadConditions;
import eu.trufchev.intargovishte.information.roadConditions.feignClients.RoadConditionsClient;
import eu.trufchev.intargovishte.information.roadConditions.repositories.RoadConditionsRepository;
import eu.trufchev.intargovishte.information.roadConditions.services.RoadConditionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RequestMapping("information/roadconditions")
@RestController
public class RoadConditionsController {
    @Autowired
    RoadConditionsClient roadConditionsClient;
    @Autowired
    RoadConditionsService roadConditionsService;
    @Autowired
    RoadConditionsRepository roadConditionsRepository;

    @Scheduled(cron = "0 0 9 * * *")
    public ResponseEntity<List<RoadConditions>> cronUpdate(){
        return updateRoadConditions();
    }

    @GetMapping("/update")
    public ResponseEntity<List<RoadConditions>> manualUpdate(){
        return updateRoadConditions();
    }

    public ResponseEntity<List<RoadConditions>> updateRoadConditions() {
        List<RoadConditions> roadConditionsList = new ArrayList<>();
        roadConditionsService.getRoadConditions().forEach(roadConditionsList::add);
        roadConditionsRepository.deleteAll();
        roadConditionsRepository.saveAll(roadConditionsList);
        return ResponseEntity.ok(roadConditionsList);
    }
    @GetMapping("/all")
    public ResponseEntity<List<RoadConditions>> allRoadCondition(){
        List<RoadConditions> roadConditions = new ArrayList<>();
        roadConditionsRepository.findAll().forEach(roadConditions::add);

        // Return the list of gas stations
        return ResponseEntity.ok(roadConditions);
    }
}