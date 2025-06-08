package eu.trufchev.intargovishte.information.events.dramaTheatre.service;

import eu.trufchev.intargovishte.information.events.dramaTheatre.entities.DramaLike;
import eu.trufchev.intargovishte.information.events.dramaTheatre.entities.Play;
import eu.trufchev.intargovishte.information.events.dramaTheatre.repository.PlaysLikeRepository;
import eu.trufchev.intargovishte.information.events.dramaTheatre.repository.PlaysRepository;
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
public class DramaLikeService {
    private final PlaysRepository playsRepository;
    private final PlaysLikeRepository playsLikeRepository;

    public DramaLikeService(PlaysRepository playsRepository, PlaysLikeRepository playsLikeRepository){
        this.playsRepository = playsRepository;
        this.playsLikeRepository = playsLikeRepository;
    }

    @Transactional
    public boolean toggleLike(Play event, User user) {
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
            Optional<DramaLike> existingLike = playsLikeRepository.findByEventAndUser(event, user);

            if (existingLike.isPresent()) {
                System.out.println("Like found. Removing like for event: " + event.getId());
                // If liked, remove the like
                playsLikeRepository.delete(existingLike.get());
                return false; // Like removed
            } else {
                System.out.println("No like found. Adding like for event: " + event.getId());
                // If not liked, add a new like
                DramaLike newLike = new DramaLike();
                newLike.setEvent(event);
                newLike.setUser(user);
                newLike.setLikedAt(LocalDateTime.now());
                // Save the new like
                playsLikeRepository.save(newLike);
                return true; // Like added
            }
        } catch (Exception e) {
            // Log the error for detailed debugging
            System.err.println("Error in toggleLike:");
            e.printStackTrace();
            throw new RuntimeException("Failed to toggle like", e);
        }
    }
    @Transactional
    public void deleteLikesForPlay(Play play) {
        playsLikeRepository.deleteAllByEvent(play);
    }
}
