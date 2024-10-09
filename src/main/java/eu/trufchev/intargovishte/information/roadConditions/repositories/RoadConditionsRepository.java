package eu.trufchev.intargovishte.information.roadConditions.repositories;

import eu.trufchev.intargovishte.information.roadConditions.entities.RoadConditions;
import org.springframework.data.repository.CrudRepository;

public interface RoadConditionsRepository extends CrudRepository<RoadConditions, Long> {
}
