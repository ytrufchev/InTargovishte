package eu.trufchev.intargovishte.information.energyOutage.repositories;

import eu.trufchev.intargovishte.information.energyOutage.entities.EnergyOutage;
import org.springframework.data.repository.CrudRepository;

public interface EnergyOutageRepository extends CrudRepository<EnergyOutage, Long> {
}
