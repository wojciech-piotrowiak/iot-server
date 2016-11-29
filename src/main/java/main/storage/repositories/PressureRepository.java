package main.storage.repositories;

import main.storage.entities.Pressure;
import org.springframework.data.repository.CrudRepository;

public interface PressureRepository extends CrudRepository<Pressure, Long> {
}
