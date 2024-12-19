package eu.trufchev.intargovishte.information.events.cinemagic.dto;

import eu.trufchev.intargovishte.information.events.cinemagic.entities.Movie;
import eu.trufchev.intargovishte.information.events.cinemagic.entities.MovieWithProjections;
import eu.trufchev.intargovishte.information.events.cinemagic.entities.Projections;
import eu.trufchev.intargovishte.information.events.cinemagic.repositories.MovieLikeRepository;
import eu.trufchev.intargovishte.information.events.cinemagic.repositories.MovieRepository;
import eu.trufchev.intargovishte.information.events.cinemagic.repositories.ProjectionRepository;
import eu.trufchev.intargovishte.user.entity.User;
import eu.trufchev.intargovishte.user.repository.UserRepository;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
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
    private final UserRepository userRepository;
    private MovieLikeRepository movieLikeRepository;


    public MovieWithProjectionsDTO(MovieRepository movieRepository, ProjectionRepository projectionRepository, UserRepository userRepository, MovieLikeRepository movieLikeRepository) {
        this.movieRepository = movieRepository;
        this.projectionRepository = projectionRepository;
        this.userRepository = userRepository;
        this.movieLikeRepository = movieLikeRepository;
    }

    public List<MovieWithProjections> combineMovieWithProjections(List<Movie> movies, List<Projections> projections) {
        Map<String, List<Projections>> projectionsByMovieId = projections.stream()
                .collect(Collectors.groupingBy(Projections::getMovieId));
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = null;
        List<MovieWithProjections> moviesWithProjections = new java.util.ArrayList<>();
            for (Movie movie : movies) {
                List<Projections> movieProjections = projectionsByMovieId.getOrDefault(movie.getId(), Collections.emptyList());
                    MovieWithProjections movieWithProjections = new MovieWithProjections();
                    movieWithProjections.setId(movie.getId());
                    movieWithProjections.setDuration(movie.getDuration());
                    movieWithProjections.setDescription(movie.getDescription());
                    movieWithProjections.setTitle(movie.getTitle());
                    movieWithProjections.setOriginalTitle(movie.getOriginalTitle());
                    movieWithProjections.setIsForChildren(movie.getIsForChildren());
                    movieWithProjections.setImdbId(movie.getImdbId());
                    movieWithProjections.setProjections(movieProjections);
                    movieWithProjections.setLikesCount(movie.getLikes() != null ? (long) movie.getLikes().size() : 0L);
                if (currentUser != null) {
                    boolean isLikedByCurrentUser = movieLikeRepository
                            .findByEventAndUser(movie, currentUser)
                            .isPresent();
                    movieWithProjections.setLikedByCurrentUser(isLikedByCurrentUser);
                } else {
                    movieWithProjections.setLikedByCurrentUser(false);
                }
                    moviesWithProjections.add(movieWithProjections);
                }
            return moviesWithProjections;
    }
}
