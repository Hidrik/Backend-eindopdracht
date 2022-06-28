package nl.novi.backend.eindopdracht.HidrikLandlust.services;

import nl.novi.backend.eindopdracht.HidrikLandlust.models.entities.Account;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AccountService {

    Account retreiveAccount(Long id);
}
