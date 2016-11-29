package main.storage.repositories;

import main.storage.entities.Humidity;
import org.springframework.data.repository.CrudRepository;

public interface HumidityRepository extends CrudRepository<Humidity, Long> {
}
