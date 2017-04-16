package main.storage.repositories;

import main.storage.entities.Pressure;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.stream.Stream;

public interface PressureRepository extends CrudRepository<Pressure, Long> {
    @Query("select p from Pressure p")
    Stream<Pressure> streamAll();
}
