package eu.trufchev.intargovishte.information.vikOutage.controllers;

import eu.trufchev.intargovishte.information.vikOutage.entities.VikOutage;
import eu.trufchev.intargovishte.information.vikOutage.repositories.VikOutageRepository;
import eu.trufchev.intargovishte.information.vikOutage.services.VikOutageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("information/vik")
public class VikOutageController {

    @Autowired
    VikOutageService vikOutageService;
    @Autowired
    VikOutageRepository vikOutageRepository;

    @Scheduled(cron = "0 0 9 * * *")
    public ResponseEntity<List<VikOutage>> cronUpdate(){
        return getTargovishteDetails();
    }
    @GetMapping("/update")
    public ResponseEntity<List<VikOutage>> manualUpdate(){
        return getTargovishteDetails();
    }
    public ResponseEntity<List<VikOutage>> getTargovishteDetails() {
        List<VikOutage> vikOutages = new ArrayList<>();
        vikOutageService.fetchAndParseVikOutage().forEach(vikOutages::add);
        vikOutageRepository.deleteAll();
        vikOutageRepository.saveAll(vikOutages);
        return ResponseEntity.ok(vikOutages);
    }
    @GetMapping("/all")
    public ResponseEntity<List<VikOutage>> getOutages() {
        List<VikOutage> vikOutages = new ArrayList<>();
        vikOutageRepository.findAll().forEach(vikOutages::add);
        return ResponseEntity.ok(vikOutages);
    }
}