package eu.trufchev.intargovishte.information.inAppInformation.repositories;

import eu.trufchev.intargovishte.information.events.appEvents.entities.EventEntity;
import eu.trufchev.intargovishte.information.events.appEvents.enums.StatusENUMS;
import eu.trufchev.intargovishte.information.inAppInformation.entity.Information;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface InformationEntityRepository extends CrudRepository<Information, Long> {

    @Query("SELECT e FROM Information e WHERE e.status = :status ORDER BY e.validto ASC")
    List<Information> findApprovedInformation(@Param("status") StatusENUMS status);

    @Query("SELECT e FROM Information e WHERE e.validTo <= :validTo")
    List<Information> findInformationBefore(@Param("validTo") String validTo);

    List<Information> findByUser(Long user);

    List<Information> findByStatus(StatusENUMS status);

}
