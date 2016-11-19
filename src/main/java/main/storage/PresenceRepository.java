package main.storage;

import main.storage.pojo.Presence;
import org.springframework.data.repository.CrudRepository;

public interface PresenceRepository extends CrudRepository<Presence, Long> {
}
