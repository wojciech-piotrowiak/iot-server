package main.storage.repositories;

import main.storage.entities.Humidity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.stream.Stream;

public interface HumidityRepository extends CrudRepository<Humidity, Long> {
    @Query("select h from Humidity h")
    Stream<Humidity> streamAll();
}
