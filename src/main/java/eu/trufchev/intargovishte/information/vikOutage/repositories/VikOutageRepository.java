package eu.trufchev.intargovishte.information.vikOutage.repositories;

import eu.trufchev.intargovishte.information.vikOutage.entities.VikOutage;
import org.springframework.data.repository.CrudRepository;

public interface VikOutageRepository extends CrudRepository<VikOutage, Long> {
}
