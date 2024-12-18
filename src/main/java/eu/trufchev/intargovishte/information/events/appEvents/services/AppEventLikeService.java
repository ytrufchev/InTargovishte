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

    // Constructor injection instead of @Autowired
    public AppEventLikeService(AppEventLikeRepository appEventLikeRepository) {
        this.appEventLikeRepository = appEventLikeRepository;
    }

    @Transactional
    public boolean toggleLike(EventEntity event, User user) {
        try {
            // Validate input
            if (event == null || user == null) {
                throw new IllegalArgumentException("Event and User must not be null");
            }

            // Check if the user has already liked the event
            Optional<AppEventLike> existingLike = appEventLikeRepository.findByEventAndUser(event, user);

            if (existingLike.isPresent()) {
                // If liked, remove the like
                appEventLikeRepository.delete(existingLike.get());
                return false; // Like removed
            } else {
                // If not liked, add a new like
                AppEventLike newLike = new AppEventLike();
                newLike.setEvent(event);
                newLike.setUser(user);

                // Explicitly save and flush
                AppEventLike savedLike = appEventLikeRepository.saveAndFlush(newLike);
                return true; // Like added
            }
        } catch (Exception e) {
            // Log the exception
            System.err.println("Error in toggleLike: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to toggle like", e);
        }
    }
}