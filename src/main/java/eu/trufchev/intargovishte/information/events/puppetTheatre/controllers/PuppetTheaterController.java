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
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/content/puppettheater")
public class PuppetTheaterController {

    @Autowired
    private PuppetTheaterService puppetTheaterService;
    @Autowired
    private PuppetTheaterRepository puppetTheaterRepository;


    @Scheduled(cron = "0 0 9 * * *")
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
        return puppetTheaters;
    }
}
