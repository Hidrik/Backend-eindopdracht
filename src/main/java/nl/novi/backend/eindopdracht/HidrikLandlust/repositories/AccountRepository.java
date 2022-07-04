package nl.novi.backend.eindopdracht.HidrikLandlust.repositories;

import nl.novi.backend.eindopdracht.HidrikLandlust.models.entities.Account;
import nl.novi.backend.eindopdracht.HidrikLandlust.models.entities.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository  extends JpaRepository<Account, Long>  {
}
