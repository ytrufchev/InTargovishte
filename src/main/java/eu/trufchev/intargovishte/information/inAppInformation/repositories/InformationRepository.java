package eu.trufchev.intargovishte.information.inAppInformation.repositories;

import eu.trufchev.intargovishte.information.events.appEvents.entities.EventEntity;
import eu.trufchev.intargovishte.information.events.appEvents.enums.StatusENUMS;
import eu.trufchev.intargovishte.information.inAppInformation.entities.Information;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface InformationRepository extends CrudRepository<Information, Long> {
    List<Information> findByStatus(StatusENUMS status);
    List<Information> findByUser(Long user);
    @Query("SELECT e FROM Information e WHERE e.date <= :yesterdayEpochSeconds")
    List<Information> findInformationBeforeEndDate(@Param("yesterdayEpochSeconds") Long yesterdayEpochSeconds);
}
