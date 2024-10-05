package eu.trufchev.intargovishte.information.events.municipality;

import eu.trufchev.intargovishte.information.events.municipality.entities.MunicipalityEvent;
import eu.trufchev.intargovishte.information.events.municipality.repository.MunicipalityEventRepository;
import eu.trufchev.intargovishte.information.events.municipality.services.MunicipalityEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/content/municipality")
public class MunicipalityEventController {
    @Autowired
    MunicipalityEventService municipalityEventService;
    @Autowired
    MunicipalityEventRepository municipalityEventRepository;

    @PostMapping("/update")
    public ResponseEntity<List<MunicipalityEvent>> createMunicipalityEvents(@RequestBody String jsonEvents) {
        List<MunicipalityEvent> municipalityEvents = (List<MunicipalityEvent>) municipalityEventService.parseAndSave(jsonEvents);
        return ResponseEntity.status(HttpStatus.CREATED).body(municipalityEvents);
    }
    @GetMapping("/all")
    public ResponseEntity<List<MunicipalityEvent>> getAllMunicipalityEvents(){
        List<MunicipalityEvent> municipalityEvents = new ArrayList<>();
        municipalityEventRepository.findAll().forEach(municipalityEvents::add);
        return ResponseEntity.ok(municipalityEvents);
    }
}