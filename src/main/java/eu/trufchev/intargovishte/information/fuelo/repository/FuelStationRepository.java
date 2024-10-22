package eu.trufchev.intargovishte.information.fuelo.repository;

import eu.trufchev.intargovishte.information.fuelo.entities.FuelStation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FuelStationRepository extends JpaRepository<FuelStation, Long> {
}
