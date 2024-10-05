package eu.trufchev.intargovishte.information.events.dramaTheatre.repository;

import eu.trufchev.intargovishte.information.events.dramaTheatre.entities.Play;
import org.springframework.data.repository.CrudRepository;

public interface PlaysRepository extends CrudRepository<Play, String> {
}
