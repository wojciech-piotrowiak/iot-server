package main.storage.repositories;

import main.storage.entities.Presence;
import org.springframework.data.repository.CrudRepository;

public interface PresenceRepository extends CrudRepository<Presence, Long> {
}
