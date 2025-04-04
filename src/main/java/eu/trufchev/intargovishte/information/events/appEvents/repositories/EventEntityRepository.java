package eu.trufchev.intargovishte.information.events.appEvents.repositories;

import eu.trufchev.intargovishte.information.events.appEvents.entities.EventEntity;
import eu.trufchev.intargovishte.information.events.appEvents.enums.StatusENUMS;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface EventEntityRepository extends CrudRepository<EventEntity, Long> {

    @Query("SELECT e FROM EventEntity e WHERE e.date >= :today AND e.status = :status ORDER BY e.date ASC")
    List<EventEntity> findNextTenApprovedEvents(@Param("today") Long today, @Param("status") StatusENUMS status);

    @Query("SELECT e FROM EventEntity e WHERE e.date <= :yesterday")
    List<EventEntity> findEventsBefore(@Param("yesterday") String yesterday);

    List<EventEntity> findByUser(Long user);

    List<EventEntity> findByStatus(StatusENUMS status);

}
