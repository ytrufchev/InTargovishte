package eu.trufchev.intargovishte.information.weather.repositories;

import eu.trufchev.intargovishte.information.weather.entities.WeatherResponse;
import org.springframework.data.repository.CrudRepository;

public interface WeatherRepository extends CrudRepository<WeatherResponse, Long> {
}
