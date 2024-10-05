package eu.trufchev.intargovishte.information.events.cinemagic.dto;

import eu.trufchev.intargovishte.information.events.cinemagic.entities.Movie;
import eu.trufchev.intargovishte.information.events.cinemagic.entities.MovieWithProjections;
import eu.trufchev.intargovishte.information.events.cinemagic.entities.Projections;
import eu.trufchev.intargovishte.information.events.cinemagic.repositories.MovieRepository;
import eu.trufchev.intargovishte.information.events.cinemagic.repositories.ProjectionRepository;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ToString
@NoArgsConstructor(force = true)
@Component
public class MovieWithProjectionsDTO {
    private final MovieRepository movieRepository;
    private final ProjectionRepository projectionRepository;

    @Autowired
    public MovieWithProjectionsDTO(MovieRepository movieRepository, ProjectionRepository projectionRepository) {
        this.movieRepository = movieRepository;
        this.projectionRepository = projectionRepository;
    }

    public List<MovieWithProjections> combineMovieWithProjections(List<Movie> movies, List<Projections> projections) {
        Map<String, List<Projections>> projectionsByMovieId = projections.stream()
                .collect(Collectors.groupingBy(Projections::getMovieId));

        List<MovieWithProjections> moviesWithProjections = new java.util.ArrayList<>();
            for (Movie movie : movies) {
                List<Projections> movieProjections = projectionsByMovieId.getOrDefault(movie.getId(), Collections.emptyList());
                    MovieWithProjections movieWithProjections = new MovieWithProjections(
                            movie.getId(),
                            movie.getDuration(),
                            movie.getDescription(),
                            movie.getTitle(),
                            movie.getOriginalTitle(),
                            movie.getIsForChildren(),
                            movie.getImdbId(),
                            movieProjections
                    );
                    moviesWithProjections.add(movieWithProjections);
                }
        return moviesWithProjections;
    }
}
