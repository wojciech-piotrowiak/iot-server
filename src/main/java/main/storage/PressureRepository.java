package main.storage;

import main.storage.pojo.Pressure;
import org.springframework.data.repository.CrudRepository;

public interface PressureRepository extends CrudRepository<Pressure, Long> {
}
