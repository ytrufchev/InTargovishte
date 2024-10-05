package eu.trufchev.intargovishte.information.events.municipality.repository;

import eu.trufchev.intargovishte.information.events.municipality.entities.MunicipalityEvent;
import org.springframework.data.repository.CrudRepository;

public interface MunicipalityEventRepository extends CrudRepository<MunicipalityEvent, Long> {
}
