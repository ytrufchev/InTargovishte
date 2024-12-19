package eu.trufchev.intargovishte.information.events.dramaTheatre.repository;

import eu.trufchev.intargovishte.information.events.dramaTheatre.entities.DramaLike;
import eu.trufchev.intargovishte.information.events.dramaTheatre.entities.Play;
import eu.trufchev.intargovishte.information.events.puppetTheatre.entities.PuppetTheater;
import eu.trufchev.intargovishte.information.events.puppetTheatre.entities.PuppetTheaterLike;
import eu.trufchev.intargovishte.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PlaysLikeRepository extends JpaRepository<DramaLike, Long> {
    List<Play> findByEventId(long eventId);
    List<Play> findByUserId(long userId);
    Optional<DramaLike> findByEventAndUser(Play event, User user);
    boolean existsByEventAndUser(Play event, User user);
    long countByEvent(Play event);
}
