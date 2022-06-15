package nl.novi.backend.eindopdracht.HidrikLandlust.repositories;

import nl.novi.backend.eindopdracht.HidrikLandlust.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
    boolean existsByUsername(String username);
}
