package main.storage.repositories;

import main.storage.entities.Temperature;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.stream.Stream;

public interface TemperatureRepository extends CrudRepository<Temperature, Long> {

    @Query("select t from Temperature t")
    Stream<Temperature> streamAll();
}
