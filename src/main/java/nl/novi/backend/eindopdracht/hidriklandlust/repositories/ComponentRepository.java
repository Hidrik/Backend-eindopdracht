package nl.novi.backend.eindopdracht.hidriklandlust.repositories;

import nl.novi.backend.eindopdracht.hidriklandlust.models.entities.Component;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ComponentRepository extends JpaRepository<Component, Long> {
}
