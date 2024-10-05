package eu.trufchev.intargovishte.information.events.cinemagic.repositories;


import eu.trufchev.intargovishte.information.events.cinemagic.entities.Movie;
import org.springframework.data.repository.CrudRepository;

public interface MovieRepository extends CrudRepository<Movie, String> {
}
