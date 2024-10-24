package eu.trufchev.intargovishte.information.events.puppetTheatre.controllers;

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
    @GetMapping("all")
    public List<PuppetTheater> allPuppetTheaterEvents(){
        List<PuppetTheater> puppetTheaters = new ArrayList<>();
        puppetTheaterRepository.findAll().forEach(puppetTheaters::add);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM d", new Locale("bg", "BG"));

        // Filter to include only events today or in the future
        List<PuppetTheater> upcomingEvents = puppetTheaters.stream()
                .filter(event -> {
                    // Build a string combining the event month and day
                    String eventDateString = event.getEventMonth() + " " + event.getEventDay();

                    // Parse the date string into a LocalDate, using the current year
                    LocalDate eventDate = LocalDate.parse(eventDateString, formatter).withYear(LocalDate.now().getYear());

                    // Return true if the event is today or in the future
                    return !eventDate.isBefore(LocalDate.now());
                })
                .collect(Collectors.toList());
                upcomingEvents.reversed();
        return upcomingEvents;
    }
}
