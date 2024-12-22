package eu.trufchev.intargovishte.information.events.cinemagic.repositories;

import eu.trufchev.intargovishte.information.events.cinemagic.entities.Movie;
import eu.trufchev.intargovishte.information.events.cinemagic.entities.MovieLike;
import eu.trufchev.intargovishte.information.events.cinemagic.entities.MovieWithProjections;
import eu.trufchev.intargovishte.information.events.puppetTheatre.entities.PuppetTheater;
import eu.trufchev.intargovishte.information.events.puppetTheatre.entities.PuppetTheaterLike;
import eu.trufchev.intargovishte.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MovieLikeRepository extends JpaRepository<MovieLike, Long> {
    List<Movie> findByEventId(String eventId);
    List<Movie> findByUserId(long userId);
    Optional<MovieLike> findByEventAndUser(Movie event, User user);
    boolean existsByEventAndUser(Movie event, User user);
    void deleteByMovieId(String movieId);
    long countByEvent(Movie event);
}
