package eu.trufchev.intargovishte.information.energyOutage.controllers;

import eu.trufchev.intargovishte.information.energyOutage.entities.EnergyOutage;
import eu.trufchev.intargovishte.information.energyOutage.repositories.EnergyOutageRepository;
import eu.trufchev.intargovishte.information.energyOutage.services.EnergyOutageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("information/energy")
public class EnergyOutageController {

    @Autowired
    EnergyOutageService energyOutageService;
    @Autowired
    EnergyOutageRepository energyOutageRepository;


    @GetMapping("/all")
    public ResponseEntity<List<EnergyOutage>> getAllEnergyOutages(){
        List<EnergyOutage> energyOutages = new ArrayList<>();
        energyOutageRepository.findAll().forEach(energyOutages::add);
        return ResponseEntity.ok(energyOutages);
    }

    @Scheduled(cron = "0 0 6 * * ?")
    public ResponseEntity<List<EnergyOutage>> updateEnergyOutages(){
        String outagesString = energyOutageService.fetchInterruptions();
        List<EnergyOutage> energyOutages = (List<EnergyOutage>) energyOutageService.updateEnergyOutages(outagesString);
        return ResponseEntity.status(HttpStatus.CREATED).body(energyOutages);
    }
    @GetMapping("/manualupdate")
    public ResponseEntity<List<EnergyOutage>> manualUpdateEnergyOutages(){
        String outagesString = energyOutageService.fetchInterruptions();
        List<EnergyOutage> energyOutages = (List<EnergyOutage>) energyOutageService.updateEnergyOutages(outagesString);
        return ResponseEntity.status(HttpStatus.CREATED).body(energyOutages);
    }
}
