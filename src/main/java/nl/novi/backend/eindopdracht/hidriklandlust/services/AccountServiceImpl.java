package nl.novi.backend.eindopdracht.hidriklandlust.services;

import nl.novi.backend.eindopdracht.hidriklandlust.dto.AccountDto;
import nl.novi.backend.eindopdracht.hidriklandlust.dto.AccountSummaryDto;
import nl.novi.backend.eindopdracht.hidriklandlust.dto.ProjectSummaryDto;
import nl.novi.backend.eindopdracht.hidriklandlust.exceptions.InternalFailureException;
import nl.novi.backend.eindopdracht.hidriklandlust.exceptions.RecordNotFoundException;
import nl.novi.backend.eindopdracht.hidriklandlust.models.entities.Account;
import nl.novi.backend.eindopdracht.hidriklandlust.models.entities.Assignment;
import nl.novi.backend.eindopdracht.hidriklandlust.models.entities.Project;
import nl.novi.backend.eindopdracht.hidriklandlust.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    AccountRepository accountRepository;

    ProjectService projectService = new ProjectServiceImpl();

    @Override
    public AccountDto getAccountDto(Long id) {
        return toAccountDto(getAccount(id));
    }

    @Override
    public AccountDto toAccountDto(Account account) {
        AccountDto dto = new AccountDto();

        dto.setId(account.getId());
        dto.setFirstName(account.getFirstName());
        dto.setLastName(account.getLastName());
        dto.setEmployeeFunction(account.getEmployeeFunction());
        dto.setAssignments(account.getAssignments());
        dto.setCity(account.getCity());
        dto.setPostalCode(account.getPostalCode());
        dto.setHouseNumber(account.getHouseNumber());
        dto.setCity(account.getCity());
        dto.setStreetName(account.getStreetName());

        if (account.getProjects() != null) {
            Set<ProjectSummaryDto> dtos = new HashSet<>();
            for (Project project : account.getProjects()) {
                dtos.add(projectService.toProjectSummaryDto(project));
            }
            dto.setProjects(dtos);
        }

        return dto;
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
    public List<Account> getAccounts() {
        return accountRepository.findAll();
    }

    @Override
    public AccountSummaryDto updateAccount(Long id, AccountSummaryDto dto) {
        Account account = getAccount(id);
        if (dto.getEmployeeFunction() != null) account.setEmployeeFunction(dto.getEmployeeFunction());
        if (dto.getFirstName() != null) account.setFirstName(dto.getFirstName());
        if (dto.getLastName() != null) account.setLastName(dto.getLastName());
        try {
            Account savedAccount = accountRepository.save(account);
            return toAccountSummaryDto(savedAccount);
        } catch (Exception e) {
            throw new InternalFailureException(String.format("Can not save account with id %s after updating.", id));
        }

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
    public void deleteAccount(Long id) {
        Account account = getAccount(id);
        try {
            accountRepository.delete(account);
        } catch (Exception e){
            throw new InternalFailureException(String.format("Can not delete account with id %s", id));
        }

    }

    @Override
    public void removeAssignmentFromAccount(Assignment assignment, Long accountId) {
        Account account = getAccount(accountId);
        account.removeAssignment(assignment);

        try {
            accountRepository.save(account);
        } catch (Exception e) {
            throw new InternalFailureException(String.format("Can not save account after with id %s after removing assignment.", accountId));
        }
    }

    @Override
    public Account getAccount(Long id) {
        Optional<Account> optionalAccount = accountRepository.findById(id);
        if (optionalAccount.isPresent()) return optionalAccount.get();

        throw new RecordNotFoundException(String.format("Account with id %s does not exist.", id));
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

        for (ProjectSummaryDto project : dto.getProjects()) {
            account.addProject(projectService.toProject(project));
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
