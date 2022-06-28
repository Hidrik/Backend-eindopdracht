package nl.novi.backend.eindopdracht.HidrikLandlust.services;

import nl.novi.backend.eindopdracht.HidrikLandlust.dto.ProjectDto;
import nl.novi.backend.eindopdracht.HidrikLandlust.exceptions.RecordNotFoundException;
import nl.novi.backend.eindopdracht.HidrikLandlust.models.entities.Account;
import nl.novi.backend.eindopdracht.HidrikLandlust.models.entities.Project;
import nl.novi.backend.eindopdracht.HidrikLandlust.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    AccountRepository accountRepository;

    @Override
    public Account retreiveAccount(Long id) {
        Optional<Account> optionalAccount = accountRepository.findById(id);
        if (optionalAccount.isPresent()) return optionalAccount.get();

        throw new RecordNotFoundException("Account with id " + id + " does not exist.");
    }
}
