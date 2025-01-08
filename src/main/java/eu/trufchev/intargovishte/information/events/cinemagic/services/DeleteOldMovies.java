package eu.trufchev.intargovishte.information.events.cinemagic.services;

import eu.trufchev.intargovishte.information.events.cinemagic.dto.MovieWithProjectionsDTO;
import eu.trufchev.intargovishte.information.events.cinemagic.entities.Movie;
import eu.trufchev.intargovishte.information.events.cinemagic.entities.MovieWithProjections;
import eu.trufchev.intargovishte.information.events.cinemagic.entities.Projections;
import eu.trufchev.intargovishte.information.events.cinemagic.repositories.MovieLikeRepository;
import eu.trufchev.intargovishte.information.events.cinemagic.repositories.MovieRepository;
import eu.trufchev.intargovishte.information.events.cinemagic.repositories.ProjectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class DeleteOldMovies {
    private final MovieRepository movieRepository;
    private final ProjectionRepository projectionRepository;
    private final MovieLikeRepository movieLikeRepository;

    @Autowired
    public DeleteOldMovies(MovieRepository movieRepository, ProjectionRepository projectionRepository, MovieLikeRepository movieLikeRepository) {
        this.movieRepository = movieRepository;
        this.projectionRepository = projectionRepository;
        this.movieLikeRepository = movieLikeRepository;
    }

    public void deleteOldMovies() {
        LocalDateTime now = LocalDateTime.now();

        List<Movie> movies = (List<Movie>) movieRepository.findAll();
        List<Projections> projections = (List<Projections>) projectionRepository.findAll();
        MovieWithProjectionsDTO movieWithProjectionsDTO = new MovieWithProjectionsDTO();
        List<MovieWithProjections> movieWithProjectionsList = movieWithProjectionsDTO.combineMovieWithProjections(movies, projections);

        for (MovieWithProjections movieWithProjections : movieWithProjectionsList) {
            if (!movieWithProjections.getProjections().isEmpty()) {
                // Get the last projection
                Projections lastProjection = movieWithProjections.getProjections()
                        .stream()
                        .max((p1, p2) -> LocalDateTime.parse(p1.getScreeningTimeTo())
                                .compareTo(LocalDateTime.parse(p2.getScreeningTimeTo())))
                        .orElse(null);

                if (lastProjection != null) {
                    LocalDateTime lastProjectionTime = LocalDateTime.parse(lastProjection.getScreeningTimeTo());

                    // Check if the last projection has passed
                    if (lastProjectionTime.isBefore(now)) {
                        movieLikeRepository.deleteByEventId(movieWithProjections.getId());
                        movieRepository.deleteById(movieWithProjections.getId());
                    }
                }
            }
        }
    }
}