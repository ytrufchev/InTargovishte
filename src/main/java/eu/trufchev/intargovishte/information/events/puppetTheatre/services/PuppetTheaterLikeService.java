package eu.trufchev.intargovishte.information.events.puppetTheatre.services;

import eu.trufchev.intargovishte.information.events.appEvents.entities.AppEventLike;
import eu.trufchev.intargovishte.information.events.appEvents.entities.EventEntity;
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
public class PuppetTheaterLikeService {
    private final PuppetTheaterRepository puppetTheaterRepository;
    private final PuppetTheaterLikeRepository puppetTheaterLikeRepository;

    public PuppetTheaterLikeService(PuppetTheaterRepository puppetTheaterRepository, PuppetTheaterLikeRepository puppetTheaterLikeRepository){
        this.puppetTheaterRepository = puppetTheaterRepository;
        this.puppetTheaterLikeRepository = puppetTheaterLikeRepository;
    }

    @Transactional
    public boolean toggleLike(PuppetTheater event, User user) {
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
            Optional<PuppetTheaterLike> existingLike = puppetTheaterLikeRepository.findByEventAndUser(event, user);

            if (existingLike.isPresent()) {
                System.out.println("Like found. Removing like for event: " + event.getId());
                // If liked, remove the like
                puppetTheaterLikeRepository.delete(existingLike.get());
                return false; // Like removed
            } else {
                System.out.println("No like found. Adding like for event: " + event.getId());
                // If not liked, add a new like
                PuppetTheaterLike newLike = new PuppetTheaterLike();
                newLike.setEvent(event);
                newLike.setUser(user);
                newLike.setLikedAt(LocalDateTime.now());
                // Save the new like
                puppetTheaterLikeRepository.save(newLike);
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
