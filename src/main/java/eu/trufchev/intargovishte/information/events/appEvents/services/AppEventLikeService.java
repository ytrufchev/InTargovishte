package eu.trufchev.intargovishte.information.events.appEvents.services;

import eu.trufchev.intargovishte.information.events.appEvents.entities.AppEventLike;
import eu.trufchev.intargovishte.information.events.appEvents.entities.EventEntity;
import eu.trufchev.intargovishte.information.events.appEvents.repositories.AppEventLikeRepository;
import eu.trufchev.intargovishte.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class AppEventLikeService {
    private final AppEventLikeRepository likeRepository;

    public AppEventLike addLike(EventEntity event, User user) {
        AppEventLike like = new AppEventLike();
        like.setEvent(event);
        like.setUser(user);
        like.setLikedAt(Instant.now());
        return likeRepository.save(like);
    }

    public void removeLikeByEventAndUser(EventEntity event, User user) {
        AppEventLike like = likeRepository.findByEventAndUser(event, user)
                .orElseThrow(() -> new IllegalArgumentException("Like not found"));
        likeRepository.delete(like);
    }

    public boolean isLikedByUser(EventEntity event, User user) {
        if (event == null || user == null) {
            throw new IllegalArgumentException("Event or User cannot be null");
        }
        return likeRepository.existsByEventAndUser(event, user);
    }
}
