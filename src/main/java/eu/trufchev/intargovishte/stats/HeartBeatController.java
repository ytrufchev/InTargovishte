package eu.trufchev.intargovishte.stats;

import eu.trufchev.intargovishte.information.events.municipality.entities.MunicipalityEvent;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("stats")
public class HeartBeatController {
    @GetMapping("/heartbeat")
    public ResponseEntity<String> getHearBeat(){
        String hearthBeat = "ok";
        return ResponseEntity.ok(hearthBeat);
    }
}
