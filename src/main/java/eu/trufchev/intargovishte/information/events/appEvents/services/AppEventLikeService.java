package eu.trufchev.intargovishte.information.events.appEvents.services;

import eu.trufchev.intargovishte.information.events.appEvents.entities.AppEventLike;
import eu.trufchev.intargovishte.information.events.appEvents.entities.EventEntity;
import eu.trufchev.intargovishte.information.events.appEvents.repositories.AppEventLikeRepository;
import eu.trufchev.intargovishte.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AppEventLikeService {
    private final AppEventLikeRepository likeRepository;

    public AppEventLike addLike(EventEntity event, User user) {
        AppEventLike like = new AppEventLike();
        like.setEvent(event.getId());
        like.setUser(user.getId());
        like.setLikedAt(Instant.now());
        return likeRepository.save(like);
    }

    public void removeLikeByEventAndUser(EventEntity event, User user) {
        Optional<AppEventLike> eventLike = likeRepository.findByEventIdAndUserId(event.getId(), user.getId());
        if(eventLike.isPresent()){
            likeRepository.delete(eventLike.get());
        }

    }

    public boolean isLikedByUser(EventEntity event, User user) {
        if (event == null || user == null) {
            throw new IllegalArgumentException("Event or User cannot be null");
        }
        return likeRepository.existsByEventAndUser(event, user);
    }
}
