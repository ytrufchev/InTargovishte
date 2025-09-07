package eu.trufchev.intargovishte.social.publication.repositories;

import eu.trufchev.intargovishte.social.publication.entities.Comment;
import org.springframework.data.repository.CrudRepository;

public interface CommentRepository extends CrudRepository<Comment, Long> {
}
