package eu.trufchev.intargovishte.information.events.cinemagic.repositories;

import eu.trufchev.intargovishte.information.events.cinemagic.entities.Projections;
import org.springframework.data.repository.CrudRepository;

public interface ProjectionRepository extends CrudRepository<Projections, Long> {
}
