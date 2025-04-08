package eu.trufchev.intargovishte.information.events.cinemagic.dto;

import eu.trufchev.intargovishte.information.events.cinemagic.entities.Movie;
import eu.trufchev.intargovishte.information.events.cinemagic.entities.MovieLike;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
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

    public List<MovieWithProjections> combineMovieWithProjections(List<Movie> movies, List<Projections> projections, User user) {
        // Map projections by movieId
        Map<String, List<Projections>> projectionsByMovieId = projections.stream()
                .collect(Collectors.groupingBy(Projections::getMovieId));

        // If authenticated, fetch user and set likedByCurrentUser based on likes
        if (user == null) {
            // Return the movie list with likedByCurrentUser set to false
            return movies.stream().map(movie -> {
                MovieWithProjections movieWithProjections = new MovieWithProjections();
                movieWithProjections.setId(movie.getId());
                movieWithProjections.setDuration(movie.getDuration());
                movieWithProjections.setDescription(movie.getDescription());
                movieWithProjections.setTitle(movie.getTitle());
                movieWithProjections.setOriginalTitle(movie.getOriginalTitle());
                movieWithProjections.setIsForChildren(movie.getIsForChildren());
                movieWithProjections.setImdbId(movie.getImdbId());
                movieWithProjections.setProjections(projectionsByMovieId.getOrDefault(movie.getId(), Collections.emptyList()));
                movieWithProjections.setLikesCount(movie.getLikes() != null ? (long) movie.getLikes().size() : 0L);
                movieWithProjections.setLikedByCurrentUser(false);
                return movieWithProjections;
            }).collect(Collectors.toList());
        }

        List<MovieWithProjections> moviesWithProjections = new ArrayList<>();
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

            // Set likedByCurrentUser based on whether the user has liked the movie
            boolean likedByCurrentUser = movieLikeRepository.existsByEventAndUser(movie, user);
            movieWithProjections.setLikedByCurrentUser(likedByCurrentUser);

            moviesWithProjections.add(movieWithProjections);
        }

        return moviesWithProjections;
    }


}
