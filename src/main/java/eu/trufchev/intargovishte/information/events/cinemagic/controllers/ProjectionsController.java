package eu.trufchev.intargovishte.information.events.cinemagic.controllers;

import eu.trufchev.intargovishte.information.events.cinemagic.dto.ProjectionsDTO;
import eu.trufchev.intargovishte.information.events.cinemagic.entities.Projections;
import eu.trufchev.intargovishte.information.events.cinemagic.feignClients.MoviesClient;
import eu.trufchev.intargovishte.information.events.cinemagic.repositories.ProjectionRepository;
import eu.trufchev.intargovishte.information.events.cinemagic.services.ProjectionsDTOToProjections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RequestMapping("content/movies")
@RestController
public class ProjectionsController {
    @Autowired
    MoviesClient moviesClient;
    @Autowired
    ProjectionRepository projectionRepository;
    public ProjectionsController(MoviesClient moviesClient){
        this.moviesClient = moviesClient;
    }


    public ResponseEntity<List<Projections>> getAllProjections(){
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime sevenDaysLater = now.plusDays(7);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

        String dateTimeFrom = now.format(formatter);
        String dateTimeTo = sevenDaysLater.format(formatter);

        List<ProjectionsDTO> projectionsDTOS = moviesClient.getAllProjections(dateTimeFrom, dateTimeTo);

        ProjectionsDTOToProjections projectionsDTOToProjections = new ProjectionsDTOToProjections();
        List<Projections> projections = projectionsDTOToProjections.toProjections(projectionsDTOS);
        projectionRepository.deleteAll();
        projectionRepository.saveAll(projections);
        return ResponseEntity.ok(projections);
    }
}
