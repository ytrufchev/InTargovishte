package eu.trufchev.intargovishte.information.events.dramaTheatre.controller;

import eu.trufchev.intargovishte.information.events.dramaTheatre.entities.Play;
import eu.trufchev.intargovishte.information.events.dramaTheatre.feignClient.CookieClient;
import eu.trufchev.intargovishte.information.events.dramaTheatre.repository.PlaysRepository;
import eu.trufchev.intargovishte.information.events.dramaTheatre.service.GetPlaysResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/content/plays")
@RestController
public class DramaTheatreController {
    @Autowired
    private PlaysRepository playsRepository;

    @Autowired
    private CookieClient playClient;

    @Autowired
    GetPlaysResponse getPlaysResponse;

    @Scheduled(cron = "0 0 9 * * *")
    public  ResponseEntity<List<Play>> cronUpdate(){
        return upcomingPlays();
    }

    @GetMapping("/update")
    public ResponseEntity<List<Play>> manualUpdate(){
        return upcomingPlays();
    }

    public ResponseEntity<List<Play>> upcomingPlays() {
        List<Play> plays = getPlaysResponse.getPlaysForTargovishte();

        playsRepository.deleteAll();  // Deletes all plays in the DB
        playsRepository.saveAll(plays);  // Save new plays to the DB
        return ResponseEntity.ok(plays);
    }
    @GetMapping("/all")
    public List<Play> playsList() {
        return (List<Play>) playsRepository.findAll();
    }
}