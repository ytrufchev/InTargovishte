package eu.trufchev.intargovishte.information.events.municipality;

import eu.trufchev.intargovishte.information.events.municipality.entities.MunicipalityEvent;
import eu.trufchev.intargovishte.information.events.municipality.repository.MunicipalityEventRepository;
import eu.trufchev.intargovishte.information.events.municipality.services.MunicipalityEventService;
import eu.trufchev.intargovishte.information.news.feignClients.CurlEvents;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/content/municipality")
public class MunicipalityEventController {
    @Autowired
    MunicipalityEventService municipalityEventService;
    @Autowired
    MunicipalityEventRepository municipalityEventRepository;

    @GetMapping("/update")
    public ResponseEntity<List<MunicipalityEvent>> createMunicipalityEvents() {
        List<MunicipalityEvent> municipalityEvents = (List<MunicipalityEvent>) municipalityEventService.parseAndSave();
        return ResponseEntity.status(HttpStatus.CREATED).body(municipalityEvents);
    }
    @GetMapping("/all")
    public ResponseEntity<List<MunicipalityEvent>> getAllMunicipalityEvents(){
        List<MunicipalityEvent> municipalityEvents = new ArrayList<>();
        municipalityEventRepository.findAll().forEach(municipalityEvents::add);
        List<MunicipalityEvent> futureEvents = municipalityEvents.stream()
                .filter(event -> {
                    // Convert the string timestamp to a long
                    long eventTimestamp = Long.parseLong(event.getDate());

                    // Convert the timestamp to LocalDate
                    LocalDate eventDate = Instant.ofEpochMilli(eventTimestamp)
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate();

                    // Return true if the event date is today or in the future
                    return !eventDate.isBefore(LocalDate.now());
                })
                .collect(Collectors.toList());
        Collections.reverse(futureEvents);
        return ResponseEntity.ok(futureEvents);
    }

}