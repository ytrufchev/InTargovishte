package eu.trufchev.intargovishte.social.publication.repositories;

import eu.trufchev.intargovishte.social.publication.entities.PublicationLike;
import org.springframework.data.repository.CrudRepository;

public interface PublicationLikeRepository extends CrudRepository<PublicationLike, Long> {
}
