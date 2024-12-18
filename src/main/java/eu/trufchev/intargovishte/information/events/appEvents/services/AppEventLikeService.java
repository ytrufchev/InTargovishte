package eu.trufchev.intargovishte.information.events.appEvents.services;

import eu.trufchev.intargovishte.information.events.appEvents.entities.AppEventLike;
import eu.trufchev.intargovishte.information.events.appEvents.entities.EventEntity;
import eu.trufchev.intargovishte.information.events.appEvents.repositories.AppEventLikeRepository;
import eu.trufchev.intargovishte.user.entity.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AppEventLikeService {
    private final AppEventLikeRepository appEventLikeRepository;

    // Constructor injection replaces @Autowired
    public AppEventLikeService(AppEventLikeRepository appEventLikeRepository) {
        this.appEventLikeRepository = appEventLikeRepository;
    }

    @Transactional
    public boolean toggleLike(EventEntity event, User user) {
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
            Optional<AppEventLike> existingLike = appEventLikeRepository.findByEventAndUser(event, user);

            if (existingLike.isPresent()) {
                System.out.println("Like found. Removing like for event: " + event.getId());
                // If liked, remove the like
                appEventLikeRepository.delete(existingLike.get());
                return false; // Like removed
            } else {
                System.out.println("No like found. Adding like for event: " + event.getId());
                // If not liked, add a new like
                AppEventLike newLike = new AppEventLike();
                newLike.setEvent(event);
                newLike.setUser(user);
                // Save the new like
                appEventLikeRepository.save(newLike);
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