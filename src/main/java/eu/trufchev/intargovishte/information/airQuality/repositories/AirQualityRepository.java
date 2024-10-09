package eu.trufchev.intargovishte.information.airQuality.repositories;

import eu.trufchev.intargovishte.information.airQuality.entities.AirQuality;
import org.springframework.data.repository.CrudRepository;

public interface AirQualityRepository extends CrudRepository<AirQuality, Long> {
}
