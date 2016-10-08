package main.storage;

import main.storage.pojo.Pressure;
import main.storage.pojo.Temperature;
import org.springframework.data.repository.CrudRepository;

public interface TemperatureRepository extends CrudRepository<Temperature, Long> {
}
