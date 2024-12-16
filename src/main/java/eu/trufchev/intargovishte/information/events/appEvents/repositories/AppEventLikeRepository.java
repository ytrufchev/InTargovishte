package eu.trufchev.intargovishte.information.events.appEvents.repositories;

import eu.trufchev.intargovishte.information.events.appEvents.entities.AppEventLike;
import eu.trufchev.intargovishte.information.events.appEvents.entities.EventEntity;
import eu.trufchev.intargovishte.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AppEventLikeRepository extends JpaRepository<AppEventLike, Long> {
        List<AppEventLike> findByEventId(long eventId);
        List<AppEventLike> findByUserId(long userId);
        Optional<AppEventLike> findByEventAndUser(EventEntity event, User user);
    boolean existsByEventAndUser(EventEntity event, User user);
    }
