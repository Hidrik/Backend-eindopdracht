package nl.novi.backend.eindopdracht.HidrikLandlust.services;

import nl.novi.backend.eindopdracht.HidrikLandlust.dto.AccountDto;
import nl.novi.backend.eindopdracht.HidrikLandlust.dto.AccountSummaryDto;
import nl.novi.backend.eindopdracht.HidrikLandlust.exceptions.InternalFailureException;
import nl.novi.backend.eindopdracht.HidrikLandlust.exceptions.RecordNotFoundException;
import nl.novi.backend.eindopdracht.HidrikLandlust.models.entities.Account;
import nl.novi.backend.eindopdracht.HidrikLandlust.models.entities.Assignment;
import nl.novi.backend.eindopdracht.HidrikLandlust.models.entities.Project;
import nl.novi.backend.eindopdracht.HidrikLandlust.repositories.AccountRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static nl.novi.backend.eindopdracht.HidrikLandlust.TestUtils.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class AccountServiceImplTest {

    @Autowired
    AccountService accountService;

    @MockBean
    AccountRepository accountRepository;

    @Test
    void getAccountSucceeds() {
        Account account = generateAccount();
        Optional<Account> optionalAccount = Optional.of(account);

        when(accountRepository.findById(any(Long.class))).thenReturn(optionalAccount);

        assertEquals(accountService.getAccount(account.getId()), account);
    }

    @Test
    void getAccountThrowsRecordNotFound() {
        Account account = generateAccount();
        Optional<Account> optionalAccount = Optional.ofNullable(null);

        when(accountRepository.findById(any(Long.class))).thenReturn(optionalAccount);

        assertThrows(RecordNotFoundException.class, () -> accountService.getAccount(account.getId()));
    }

    @Test
    void getAccountsSucceeds() {
        Account account = generateAccount();
        List<Account> accounts = new ArrayList<>();
        accounts.add(account);

        when(accountRepository.findAll()).thenReturn(accounts);

        assertEquals(accountService.getAccounts(), accounts);
    }

    @Test
    void getAccountDtoSucceeds() {
        Account account = generateAccount();
        Optional<Account> optionalAccount = Optional.of(account);
        AccountDto dto = generateAccountDto();
        //dto.setProjects(account.getProjects()); //So that they are same instance for asserting equal.

        when(accountRepository.findById(any(Long.class))).thenReturn(optionalAccount);

        assertThat(accountService.getAccountDto(account.getId()))
                .usingRecursiveComparison()
                .ignoringFields("projects")
                .isEqualTo(dto);
    }

    @Test
    void getAccountDtoThrowsNotFoundException() {
        Account account = generateAccount();
        Optional<Account> optionalAccount = Optional.ofNullable(null);

        when(accountRepository.findById(any(Long.class))).thenReturn(optionalAccount);

        assertThrows(RecordNotFoundException.class, () -> accountService.getAccountDto(account.getId()));

    }

    @Test
    void getAccountSummaryDtoSucceeds() {
        Account account = generateAccount();
        Optional<Account> optionalAccount = Optional.of(account);
        AccountSummaryDto dto = generateAccountSummaryDto();

        when(accountRepository.findById(any(Long.class))).thenReturn(optionalAccount);

        assertThat(accountService.getAccountSummaryDto(account.getId()))
                .usingRecursiveComparison()
                .isEqualTo(dto);
    }

    @Test
    void getAccountSummaryDtoThrowsNotFoundException() {
        Account account = generateAccount();
        Optional<Account> optionalAccount = Optional.ofNullable(null);

        when(accountRepository.findById(any(Long.class))).thenReturn(optionalAccount);

        assertThrows(RecordNotFoundException.class, () -> accountService.getAccountSummaryDto(account.getId()));
    }

    @Test
    void getAccountsSummaryDtoSucceeds() {
        Account account = generateAccount();
        List<Account> accounts = new ArrayList<>();
        accounts.add(account);

        AccountSummaryDto dto = generateAccountSummaryDto();
        List<AccountSummaryDto> dtos = new ArrayList<>();
        dtos.add(dto);

        when(accountRepository.findAll()).thenReturn(accounts);

        assertThat(accountService.getAccountsSummaryDto())
                .usingRecursiveComparison()
                .isEqualTo(dtos);
    }

    @Test
    void getAccountsSummaryDtoSucceedsListLength0() {
        List<Account> accounts = new ArrayList<>();

        List<AccountSummaryDto> dtos = new ArrayList<>();

        when(accountRepository.findAll()).thenReturn(accounts);

        assertThat(accountService.getAccountsSummaryDto())
                .usingRecursiveComparison()
                .isEqualTo(dtos);
    }

    @Test
    void updateAccountSucceedsWhenDtoHasNoNull() {
        AccountSummaryDto dto = generateAccountSummaryDto();
        dto.setEmployeeFunction("Test1");
        dto.setFirstName("Test2");
        dto.setLastName("Test3");

        Account account = generateAccount();
        Optional<Account> optionalAccount = Optional.of(account);

        Account updatedAccount = generateAccount();
        updatedAccount.setEmployeeFunction(dto.getEmployeeFunction());
        updatedAccount.setFirstName(dto.getFirstName());
        updatedAccount.setLastName(dto.getLastName());

        when(accountRepository.findById(any(Long.class))).thenReturn(optionalAccount);
        when(accountRepository.save(any(Account.class))).thenReturn(updatedAccount);

        assertThat(accountService.updateAccount(dto.getId(), dto))
                .usingRecursiveComparison()
                .isEqualTo(dto);
    }

    @Test
    void updateAccountSucceedsWhenDtoHasSomeFieldsNotFilled() {
        AccountSummaryDto dto = generateAccountSummaryDto();
        AccountSummaryDto outcomeDto = generateAccountSummaryDto();
        Account account = generateAccount();
        Optional<Account> optionalAccount = Optional.of(account);

        dto.setEmployeeFunction(null);
        dto.setFirstName(null);
        dto.setLastName(null);

        when(accountRepository.findById(any(Long.class))).thenReturn(optionalAccount);
        when(accountRepository.save(any(Account.class))).thenReturn(account);

        assertThat(accountService.updateAccount(dto.getId(), dto))
                .usingRecursiveComparison()
                .isEqualTo(outcomeDto);
    }

    @Test
    void updateAccountThrowsInternalFailureExceptionWhenSavingThrowsRuntimeException() {
        AccountSummaryDto dto = generateAccountSummaryDto();
        Account account = generateAccount();
        Optional<Account> optionalAccount = Optional.of(account);

        when(accountRepository.findById(any(Long.class))).thenReturn(optionalAccount);
        when(accountRepository.save(any(Account.class))).thenThrow(new RuntimeException());

        assertThrows(InternalFailureException.class, () -> accountService.updateAccount(dto.getId(), dto));
    }

    @Test
    void deleteAccountSucceeds() {
        Account account = generateAccount();
        Optional<Account> optionalAccount = Optional.of(account);

        doNothing().when(accountRepository).delete(any(Account.class));

        when(accountRepository.findById(any(Long.class))).thenReturn(optionalAccount);

        assertDoesNotThrow(() -> accountService.deleteAccount(account.getId()));
    }

    @Test
    void deleteAccountThrowsInternalFailureExceptionWhenSavingFails() {
        Account account = generateAccount();
        Optional<Account> optionalAccount = Optional.of(account);

        when(accountRepository.findById(any(Long.class))).thenReturn(optionalAccount);
        doThrow(new RuntimeException()).when(accountRepository).delete(any(Account.class));

        assertThrows(InternalFailureException.class, () -> accountService.deleteAccount(account.getId()));
    }

    @Test
    void removeAssignmentFromAccountSucceeds() {
        Account account = generateAccount();
        Assignment assignment = account.getAssignments().stream().iterator().next();
        Optional<Account> optionalAccount = Optional.of(account);

        when(accountRepository.findById(any(Long.class))).thenReturn(optionalAccount);
        when(accountRepository.save(any(Account.class))).thenReturn(account);

        assertDoesNotThrow(() -> accountService.removeAssignmentFromAccount(assignment, account.getId()));
    }

    @Test
    void removeAssignmentFromAccountThrowsInternalFailureExceptionAfterSavingFails() {
        Account account = generateAccount();
        Assignment assignment = account.getAssignments().stream().iterator().next();
        Optional<Account> optionalAccount = Optional.of(account);

        when(accountRepository.findById(any(Long.class))).thenReturn(optionalAccount);
        when(accountRepository.save(any(Account.class))).thenThrow(new RuntimeException());

        assertThrows(InternalFailureException.class,
                () -> accountService.removeAssignmentFromAccount(assignment, account.getId()));
    }

    @Test
    void toAccountDtoFromAccountSucceeds() {
        AccountDto dto = generateAccountDto();
        Account account = generateAccount();
        //dto.setProjects(account.getProjects()); //So that they are the same instance when asserting projects equal.

        assertThat(accountService.toAccountDto(account))
                .usingRecursiveComparison()
                .ignoringFields("projects")
                .isEqualTo(dto);
    }

    @Test
    void toAccountSummaryDtoFromAccountSucceeds() {
        AccountSummaryDto dto = generateAccountSummaryDto();
        Account account = generateAccount();

        assertThat(accountService.toAccountSummaryDto(account))
                .usingRecursiveComparison()
                .isEqualTo(dto);
    }

    @Test
    void toAccountFromAccountDtoSucceeds() {
        AccountDto dto = generateAccountDto();
        Account account = generateAccount();
        account.setCreatedOn(null); // GenerateAccount also generates createdOn & updatedOn value, DTO does not have this attribute
        account.setUpdatedOn(null); // so accountService.toAccount always return's createdOn & updatedOn as null.

        assertThat(accountService.toAccount(dto))
                .usingRecursiveComparison()
                .ignoringFields("projects")
                .isEqualTo(account);
    }

    @Test
    void toAccountFromAccountSummaryDtoSucceeds() {
        AccountSummaryDto dto = generateAccountSummaryDto();
        Account account = generateAccount();

        account.setAssignments(new HashSet<>()); //accountService.toAccount returns empty assignments list
        account.setProjects(new HashSet<>()); //accountService.toAccount returns empty projects list

        //DTO does not have this attributes so set null before asserting equal.
        account.setCity(null);
        account.setHouseNumber(null);
        account.setPostalCode(null);
        account.setStreetName(null);

        account.setCreatedOn(null); // GenerateAccount also generates createdOn & updatedOn value, DTO does not have this attribute
        account.setUpdatedOn(null); // so accountService.toAccount always return's createdOn & updatedOn as null.

        assertThat(accountService.toAccount(dto))
                .usingRecursiveComparison()
                .isEqualTo(account);
    }
}
