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

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class DeleteOldMovies {

    private final MovieRepository movieRepository;
    private final ProjectionRepository projectionRepository;

    @Autowired
    public DeleteOldMovies(MovieRepository movieRepository, ProjectionRepository projectionRepository) {
        this.movieRepository = movieRepository;
        this.projectionRepository = projectionRepository;
    }

    public void deleteOldMovies() {
        ZonedDateTime now = ZonedDateTime.now();

        // Fetch movies and their projections
        List<Movie> movies = new ArrayList<>();
        movieRepository.findAll().forEach(movies::add);
        List<Projections> projections = new ArrayList<>();
        projectionRepository.findAll().forEach(projections::add);

        // Combine movies and projections
        MovieWithProjectionsDTO movieWithProjectionsDTO = new MovieWithProjectionsDTO();
        List<MovieWithProjections> movieWithProjectionsList = movieWithProjectionsDTO.combineMovieWithProjections(movies, projections, null);

        // Process each movie with its projections
        for (MovieWithProjections movieWithProjections : movieWithProjectionsList) {
            // Find the last projection
            Projections lastProjection = movieWithProjections.getProjections().stream()
                    .max((p1, p2) -> ZonedDateTime.parse(p1.getScreeningTimeTo())
                            .compareTo(ZonedDateTime.parse(p2.getScreeningTimeTo())))
                    .orElse(null);

            if (lastProjection != null) {
                ZonedDateTime lastProjectionTime = ZonedDateTime.parse(lastProjection.getScreeningTimeTo());

                // Check if the last projection has ended
                if (lastProjectionTime.isBefore(now)) {
                    try {
                        // Delete the movie (associated likes will be deleted automatically)
                        movieRepository.deleteById(movieWithProjections.getId());
                    } catch (Exception e) {
                        // Log the error and continue processing other movies
                        System.err.println("Error deleting movie with ID " + movieWithProjections.getId() + ": " + e.getMessage());
                    }
                }
            }
        }

        // Handle movies with no projections
        for (Movie movie : movies) {
            boolean hasProjections = projections.stream()
                    .anyMatch(projection -> projection.getMovieId().equals(movie.getId()));

            if (!hasProjections) {
                try {
                    // Delete the movie with no projections
                    movieRepository.deleteById(movie.getId());
                } catch (Exception e) {
                    // Log the error and continue processing other movies
                    System.err.println("Error deleting movie with no projections and ID " + movie.getId() + ": " + e.getMessage());
                }
            }
        }
    }
}