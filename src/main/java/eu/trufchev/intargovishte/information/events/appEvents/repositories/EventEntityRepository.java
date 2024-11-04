package eu.trufchev.intargovishte.information.events.appEvents.repositories;

import eu.trufchev.intargovishte.information.events.appEvents.entities.EventEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface EventEntityRepository extends CrudRepository<EventEntity, Long> {
    @Query("SELECT e FROM EventEntity e WHERE e.date >= :today ORDER BY e.date ASC")
    List<EventEntity> findNextTenEvents(@Param("today") Long today);

    @Query("SELECT e FROM EventEntity e WHERE e.date <= :yesterday")
    List<EventEntity> findEventsBefore(@Param("yesterday") LocalDate yesterday);

    List<EventEntity> findByUser(Long user);
}
