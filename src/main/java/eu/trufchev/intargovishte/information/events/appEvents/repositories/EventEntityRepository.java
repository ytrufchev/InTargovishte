package eu.trufchev.intargovishte.information.events.appEvents.repositories;

import eu.trufchev.intargovishte.information.events.appEvents.entities.EventEntity;
import org.springframework.data.repository.CrudRepository;

public interface EventEntityRepository extends CrudRepository<EventEntity, Long> {
}
