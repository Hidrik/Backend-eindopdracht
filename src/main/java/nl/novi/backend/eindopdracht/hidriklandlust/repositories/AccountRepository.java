package nl.novi.backend.eindopdracht.hidriklandlust.repositories;

import nl.novi.backend.eindopdracht.hidriklandlust.models.entities.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {
}
