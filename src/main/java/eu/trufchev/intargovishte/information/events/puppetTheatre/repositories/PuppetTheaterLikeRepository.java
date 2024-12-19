package eu.trufchev.intargovishte.information.events.puppetTheatre.repositories;

import eu.trufchev.intargovishte.information.events.appEvents.entities.AppEventLike;
import eu.trufchev.intargovishte.information.events.appEvents.entities.EventEntity;
import eu.trufchev.intargovishte.information.events.puppetTheatre.entities.PuppetTheater;
import eu.trufchev.intargovishte.information.events.puppetTheatre.entities.PuppetTheaterLike;
import eu.trufchev.intargovishte.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PuppetTheaterLikeRepository extends JpaRepository<PuppetTheaterLike, Long> {
    List<PuppetTheater> findByEventId(long eventId);
    List<PuppetTheater> findByUserId(long userId);
    Optional<PuppetTheaterLike> findByEventAndUser(PuppetTheater event, User user);
    boolean existsByEventAndUser(PuppetTheater event, User user);
    long countByEvent(PuppetTheater event);
}
