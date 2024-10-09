package eu.trufchev.intargovishte.information.news.repositories;

import eu.trufchev.intargovishte.information.news.entities.News;
import org.springframework.data.repository.CrudRepository;

public interface NewsRepository extends CrudRepository<News, Long> {
}
