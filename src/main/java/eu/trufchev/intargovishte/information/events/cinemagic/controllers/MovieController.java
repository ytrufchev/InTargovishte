package eu.trufchev.intargovishte.information.events.cinemagic.controllers;

import eu.trufchev.intargovishte.information.events.cinemagic.entities.Movie;
import eu.trufchev.intargovishte.information.events.cinemagic.repositories.MovieLikeRepository;
import eu.trufchev.intargovishte.information.events.cinemagic.repositories.MovieRepository;
import  eu.trufchev.intargovishte.information.events.cinemagic.feignClients.MoviesClient;
import eu.trufchev.intargovishte.information.events.cinemagic.dto.MovieDTO;
import eu.trufchev.intargovishte.information.events.cinemagic.repositories.ProjectionRepository;
import eu.trufchev.intargovishte.information.events.cinemagic.services.DeleteOldMovies;
import eu.trufchev.intargovishte.information.events.cinemagic.services.MovieDTOToMovie;
import eu.trufchev.intargovishte.information.events.cinemagic.services.MovieLikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
@RequestMapping("content/movies")
@RestController
public class MovieController {
    @Autowired
    private MovieRepository movieRepository;
    @Autowired
    MovieLikeRepository movieLikeRepository;
    @Autowired
    ProjectionRepository projectionsRepository;
    @Autowired
    DeleteOldMovies deleteOldMovies;
    @Autowired
    ProjectionsController projectionsController;

    private final MoviesClient moviesClient;

    public MovieController(MoviesClient moviesClient){
        this.moviesClient = moviesClient;
    }

    @GetMapping("/list")
    public List<Movie> moviesList(){
        return (List<Movie>) movieRepository.findAll();
    }

    @Scheduled(cron = "0 0 9 * * *")
    public void updateMovies() {
        updateMovieList();
    }

    // Manual update endpoint
    @GetMapping("/update")
    public ResponseEntity<List<Movie>> manualUpdateMovies() {
        return updateMovieList();
    }

    private ResponseEntity<List<Movie>> updateMovieList() {
        DeleteOldMovies deleteOldMovies = new DeleteOldMovies(movieRepository, projectionsRepository);
        deleteOldMovies.deleteOldMovies();
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime sevenDaysLater = now.plusDays(7);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

        String dateTimeFrom = now.format(formatter);
        String dateTimeTo = sevenDaysLater.format(formatter);

        String cinemaId = "e20a0129-2388-4822-b4a4-a5cc8e119f4b";
        List<MovieDTO> movieDTO = moviesClient.getMovies(dateTimeFrom, dateTimeTo, cinemaId);
        MovieDTOToMovie movieDTOToMovie = new MovieDTOToMovie();
        List<Movie> movies = movieDTOToMovie.toMovie(movieDTO);

        movieRepository.saveAll(movies);
        projectionsController.getAllProjections();
        return ResponseEntity.ok(movies);
    }
}
