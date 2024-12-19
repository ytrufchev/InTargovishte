package eu.trufchev.intargovishte.information.events.cinemagic.services;

import eu.trufchev.intargovishte.information.events.appEvents.entities.AppEventLike;
import eu.trufchev.intargovishte.information.events.appEvents.entities.EventEntity;
import eu.trufchev.intargovishte.information.events.cinemagic.entities.Movie;
import eu.trufchev.intargovishte.information.events.cinemagic.entities.MovieLike;
import eu.trufchev.intargovishte.information.events.cinemagic.repositories.MovieLikeRepository;
import eu.trufchev.intargovishte.information.events.cinemagic.repositories.MovieRepository;
import eu.trufchev.intargovishte.information.events.puppetTheatre.entities.PuppetTheater;
import eu.trufchev.intargovishte.information.events.puppetTheatre.entities.PuppetTheaterLike;
import eu.trufchev.intargovishte.information.events.puppetTheatre.repositories.PuppetTheaterLikeRepository;
import eu.trufchev.intargovishte.information.events.puppetTheatre.repositories.PuppetTheaterRepository;
import eu.trufchev.intargovishte.user.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class MovieLikeService {
    private final MovieRepository movieRepository;
    private final MovieLikeRepository movieLikeRepository;

    public MovieLikeService(MovieRepository movieRepository, MovieLikeRepository movieLikeRepository){
        this.movieRepository = movieRepository;
        this.movieLikeRepository = movieLikeRepository;
    }

    @Transactional
    public boolean toggleLike(Movie event, User user) {
        // Validate input
        if (event == null) {
            throw new IllegalArgumentException("Event must not be null");
        }
        if (user == null) {
            throw new IllegalArgumentException("User must not be null");
        }

        try {
            System.out.println("Checking if the event is liked by user: " + user.getUsername());
            // Check if the user has already liked the event
            Optional<MovieLike> existingLike = movieLikeRepository.findByEventAndUser(event, user);

            if (existingLike.isPresent()) {
                System.out.println("Like found. Removing like for event: " + event.getId());
                // If liked, remove the like
                movieLikeRepository.delete(existingLike.get());
                return false; // Like removed
            } else {
                System.out.println("No like found. Adding like for event: " + event.getId());
                // If not liked, add a new like
                MovieLike newLike = new MovieLike();
                newLike.setEvent(event);
                newLike.setUser(user);
                newLike.setLikedAt(LocalDateTime.now());
                // Save the new like
                movieLikeRepository.save(newLike);
                return true; // Like added
            }
        } catch (Exception e) {
            // Log the error for detailed debugging
            System.err.println("Error in toggleLike:");
            e.printStackTrace();
            throw new RuntimeException("Failed to toggle like", e);
        }
    }
}
