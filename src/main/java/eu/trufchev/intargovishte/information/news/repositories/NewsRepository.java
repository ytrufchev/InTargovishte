package eu.trufchev.intargovishte.information.news.repositories;

import eu.trufchev.intargovishte.information.news.entities.News;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface NewsRepository extends CrudRepository<News, Long> {
    boolean existsById(@NotNull Long id);

    @Query(value = "SELECT n FROM News n ORDER BY n.date DESC")
    List<News> findLatestNewsLimited();

    @Query(value = "SELECT n FROM News n ORDER BY n.date DESC")
    List<News> findLatestNews();
}
