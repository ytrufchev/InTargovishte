package eu.trufchev.intargovishte.information.events.puppetTheatre.controllers;

import eu.trufchev.intargovishte.information.events.puppetTheatre.PuppetTheaterDTO;
import eu.trufchev.intargovishte.information.events.puppetTheatre.entities.PuppetTheater;
import eu.trufchev.intargovishte.information.events.puppetTheatre.repositories.PuppetTheaterRepository;
import eu.trufchev.intargovishte.information.events.puppetTheatre.services.PuppetTheaterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/content/puppettheater")
public class PuppetTheaterController {

    @Autowired
    private PuppetTheaterService puppetTheaterService;
    @Autowired
    private PuppetTheaterRepository puppetTheaterRepository;


    @Scheduled(cron = "0 0 9 * * *")
    public List<PuppetTheater> cronUpdate() throws IOException{
        return updatePuppetTheaterEvents();
    }

    @GetMapping("/update")
    public List<PuppetTheater> manualUpdate() throws IOException{
        return updatePuppetTheaterEvents();
    }

    public List<PuppetTheater> updatePuppetTheaterEvents() throws IOException {
        List<PuppetTheater> puppetTheater = new ArrayList<>();
        puppetTheaterService.getTheaterEvents().forEach(puppetTheater::add);
        puppetTheaterRepository.deleteAll();
        puppetTheaterRepository.saveAll(puppetTheater);
        return puppetTheater;
    }
    @GetMapping("/all")
    public List<PuppetTheaterDTO> allPuppetTheaterEvents(){
        LocalDate currentDate = LocalDate.now();
        int currentYear = currentDate.getYear();
        List<PuppetTheaterDTO> allEvents = puppetTheaterService.PuppetToDTO();
        // Create formatter for Bulgarian locale
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM d yyyy", new Locale("bg"));

        return allEvents.stream()
                .filter(event -> {
                    try {
                        // Parse the event date using Bulgarian locale
                        String dateStr = String.format("%s %s %d",
                                event.getEventMonth(),
                                event.getEventDay(),
                                currentYear);
                        LocalDate eventDate = LocalDate.parse(dateStr, formatter);

                        // If the event date is in the past for this year, assume it's for next year
                        if (eventDate.isBefore(currentDate)) {
                            eventDate = eventDate.plusYears(1);
                        }

                        // Return true if the event is today or in the future
                        return !eventDate.isBefore(currentDate);
                    } catch (Exception e) {
                        // Log the error if needed
                        // logger.error("Error parsing date for event: " + event.getId(), e);
                        return false;
                    }
                })
                .collect(Collectors.toList());
    }
}
