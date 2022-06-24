package nl.novi.backend.eindopdracht.HidrikLandlust.repositories;

import nl.novi.backend.eindopdracht.HidrikLandlust.models.entities.Authority;
import nl.novi.backend.eindopdracht.HidrikLandlust.models.entities.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorityRepository  extends JpaRepository<Authority, String> {
}
