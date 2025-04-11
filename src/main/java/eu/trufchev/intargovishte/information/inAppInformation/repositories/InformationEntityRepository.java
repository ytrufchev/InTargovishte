package eu.trufchev.intargovishte.information.inAppInformation.repositories;

import eu.trufchev.intargovishte.information.events.appEvents.entities.EventEntity;
import eu.trufchev.intargovishte.information.events.appEvents.enums.StatusENUMS;
import eu.trufchev.intargovishte.information.inAppInformation.entity.Information;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface InformationEntityRepository extends CrudRepository<Information, Long> {

    @Query("SELECT e FROM Information e WHERE e.date >= :today AND e.status = :status ORDER BY e.date ASC")
    List<Information> findNextTenApprovedInformation(@Param("today") Long today, @Param("status") StatusENUMS status);

    @Query("SELECT e FROM Information e WHERE e.date <= :yesterday")
    List<Information> findInformationBefore(@Param("yesterday") String yesterday);

    List<Information> findByUser(Long user);

    List<Information> findByStatus(StatusENUMS status);

}
