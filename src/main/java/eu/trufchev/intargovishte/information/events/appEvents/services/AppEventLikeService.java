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

@RequiredArgsConstructor
@Service
public class AppEventLikeService {
    @Autowired
    private AppEventLikeRepository appEventLikeRepository;

    @Transactional
    public boolean toggleLike(EventEntity event, User user) {
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
            appEventLikeRepository.save(newLike);
            return true; // Like added
        }
    }
}
