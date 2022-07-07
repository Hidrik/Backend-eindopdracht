package nl.novi.backend.eindopdracht.hidriklandlust.repositories;

import nl.novi.backend.eindopdracht.hidriklandlust.models.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
    boolean existsByEmail(String email);
}
