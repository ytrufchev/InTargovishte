package eu.trufchev.intargovishte.information.events.cinemagic.controllers;

import eu.trufchev.intargovishte.information.events.cinemagic.dto.MovieWithProjectionsDTO;
import eu.trufchev.intargovishte.information.events.cinemagic.entities.Movie;
import eu.trufchev.intargovishte.information.events.cinemagic.entities.MovieWithProjections;
import eu.trufchev.intargovishte.information.events.cinemagic.entities.Projections;
import eu.trufchev.intargovishte.information.events.cinemagic.repositories.MovieRepository;
import eu.trufchev.intargovishte.information.events.cinemagic.repositories.ProjectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@RequestMapping("content/movies")
@RestController
public class moviesWithProjectionController {
    @Autowired
    MovieRepository movieRepository;
    @Autowired
    ProjectionRepository projectionRepository;

    @GetMapping("/all")
    public ResponseEntity<List<MovieWithProjections>> screenings(){
        List<Movie> movies = (List<Movie>) movieRepository.findAll();
        List<Projections> projections = (List<Projections>) projectionRepository.findAll();
        MovieWithProjectionsDTO movieWithProjectionsDTO = new MovieWithProjectionsDTO();
    List<MovieWithProjections> movieWithProjectionsList = movieWithProjectionsDTO.combineMovieWithProjections(movies, projections);
    return ResponseEntity.ok(movieWithProjectionsList);
    }

}
