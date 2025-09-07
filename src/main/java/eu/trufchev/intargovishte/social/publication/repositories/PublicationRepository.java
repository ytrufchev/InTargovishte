package eu.trufchev.intargovishte.social.publication.repositories;

import eu.trufchev.intargovishte.social.publication.entities.Publication;
import org.springframework.data.repository.CrudRepository;

public interface PublicationRepository extends CrudRepository<Publication, Long> {
}
