package nl.novi.backend.eindopdracht.HidrikLandlust.controllers;

import nl.novi.backend.eindopdracht.HidrikLandlust.dto.AccountDto;
import nl.novi.backend.eindopdracht.HidrikLandlust.dto.AccountSummaryDto;
import nl.novi.backend.eindopdracht.HidrikLandlust.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping(value = "/accounts")
public class AccountController {

    @Autowired
    AccountService accountService;

    @GetMapping(value = "")
    public ResponseEntity<List<AccountSummaryDto>> getAccounts() {

        List<AccountSummaryDto> dtos = accountService.getAccountSummaryDto();

        return ResponseEntity.ok().body(dtos);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<AccountDto> getAccount(@PathVariable("id") Long id) {

        AccountDto dto = accountService.getAccountDto(id);

        return ResponseEntity.ok().body(dto);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<AccountDto> updateAccount(@PathVariable("id") Long id, @RequestBody AccountSummaryDto dto) {

        accountService.updateAccount(id, dto);

        return ResponseEntity.ok().build();
    }
}
