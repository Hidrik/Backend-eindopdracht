package nl.novi.backend.eindopdracht.HidrikLandlust.services;

import nl.novi.backend.eindopdracht.HidrikLandlust.dto.AccountDto;
import nl.novi.backend.eindopdracht.HidrikLandlust.dto.AccountSummaryDto;
import nl.novi.backend.eindopdracht.HidrikLandlust.models.entities.Account;
import nl.novi.backend.eindopdracht.HidrikLandlust.models.entities.Assignment;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AccountService {

    Account getAccount(Long id);

    List<Account> getAccounts();

    AccountDto getAccountDto(Long id);

    AccountSummaryDto getAccountSummaryDto(Long id);

    List<AccountSummaryDto> getAccountsSummaryDto();

    AccountSummaryDto updateAccount(Long id, AccountSummaryDto dto);

    void deleteAccount(Long id);

    void removeAssignmentFromAccount(Assignment assignment, Long accountId);

    AccountDto toAccountDto(Account account);

    AccountSummaryDto toAccountSummaryDto(Account account);

    Account toAccount(AccountDto dto);

    Account toAccount(AccountSummaryDto dto);

}
