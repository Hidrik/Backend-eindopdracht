package nl.novi.backend.eindopdracht.HidrikLandlust.services;

import nl.novi.backend.eindopdracht.HidrikLandlust.dto.AccountDto;
import nl.novi.backend.eindopdracht.HidrikLandlust.dto.AccountSummaryDto;
import nl.novi.backend.eindopdracht.HidrikLandlust.dto.ProjectDto;
import nl.novi.backend.eindopdracht.HidrikLandlust.exceptions.RecordNotFoundException;
import nl.novi.backend.eindopdracht.HidrikLandlust.models.entities.Account;
import nl.novi.backend.eindopdracht.HidrikLandlust.models.entities.Assignment;
import nl.novi.backend.eindopdracht.HidrikLandlust.models.entities.Project;
import nl.novi.backend.eindopdracht.HidrikLandlust.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    AccountRepository accountRepository;

    @Override
    public Account getAccount(Long id) {
        Optional<Account> optionalAccount = accountRepository.findById(id);
        if (optionalAccount.isPresent()) return optionalAccount.get();

        throw new RecordNotFoundException(String.format("Account with id %s does not exist.", id));
    }

    @Override
    public List<Account> getAccounts() {
        return accountRepository.findAll();
    }

    @Override
    public AccountDto getAccountDto(Long id) {
        return toAccountDto(getAccount(id));
    }

    @Override
    public AccountSummaryDto getAccountSummaryDto(Long id) {
        return toAccountSummaryDto(getAccount(id));
    }

    @Override
    public List<AccountSummaryDto> getAccountsSummaryDto() {
        List<AccountSummaryDto> dtos = new ArrayList<>();
        List<Account> accounts = getAccounts();
        if (accounts.size() > 0) {
            for (Account account : accounts) {
                dtos.add(toAccountSummaryDto(account));
            }
        }
        return dtos;
    }

    @Override
    public AccountSummaryDto updateAccount(Long id, AccountSummaryDto dto) {
        Account account = getAccount(id);
        if (dto.getEmployeeFunction() != null) account.setEmployeeFunction(dto.getEmployeeFunction());
        if (dto.getFirstName() != null) account.setFirstName(dto.getFirstName());
        if (dto.getLastName() != null) account.setLastName(dto.getLastName());

        Account savedAccount = accountRepository.save(account);
        return toAccountSummaryDto(savedAccount);
    }

    @Override
    public void deleteAccount(Long id) {
        Optional<Account> optionalAccount = accountRepository.findById(id);
        if (optionalAccount.isPresent()) accountRepository.delete(optionalAccount.get());
    }

    @Override
    public void removeAssignmentFromAccount(Assignment assignment, Long accountId) {
        Account account = getAccount(accountId);
        account.removeAssignment(assignment);
        accountRepository.save(account);
    }

    @Override
    public AccountDto toAccountDto(Account account) {
        AccountDto dto = new AccountDto();

        dto.setId(account.getId());
        dto.setFirstName(account.getFirstName());
        dto.setLastName(account.getLastName());
        dto.setEmployeeFunction(account.getEmployeeFunction());
        dto.setAssignments(account.getAssignments());
        dto.setProjects(account.getProjects());
        dto.setCity(account.getCity());
        dto.setPostalCode(account.getPostalCode());
        dto.setHouseNumber(account.getHouseNumber());
        dto.setCity(account.getCity());
        dto.setStreetName(account.getStreetName());
        return dto;
    }

    @Override
    public AccountSummaryDto toAccountSummaryDto(Account account) {
        AccountSummaryDto dto = new AccountSummaryDto();
        dto.setId(account.getId());
        dto.setFirstName(account.getFirstName());
        dto.setLastName(account.getLastName());
        dto.setEmployeeFunction(account.getEmployeeFunction());
        return dto;
    }

    @Override
    public Account toAccount(AccountDto dto) {
        Account account = new Account();
        account.setId(dto.getId());
        account.setFirstName(dto.getFirstName());
        account.setLastName(dto.getLastName());
        account.setEmployeeFunction(dto.getEmployeeFunction());
        account.setCity(dto.getCity());
        account.setPostalCode(dto.getPostalCode());
        account.setHouseNumber(dto.getHouseNumber());
        account.setCity(dto.getCity());
        account.setStreetName(dto.getStreetName());

        for (Assignment assignment : dto.getAssignments()) {
            account.addAssignment(assignment);
        }

        for (Project project : dto.getProjects()) {
            account.addProject(project);
        }

        return account;
    }

    @Override
    public Account toAccount(AccountSummaryDto dto) {
        Account account = new Account();
        account.setId(dto.getId());
        account.setFirstName(dto.getFirstName());
        account.setLastName(dto.getLastName());
        account.setEmployeeFunction(dto.getEmployeeFunction());

        return account;
    }
}
