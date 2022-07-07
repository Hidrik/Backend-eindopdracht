package nl.novi.backend.eindopdracht.hidriklandlust.controllers;

import nl.novi.backend.eindopdracht.hidriklandlust.TestUtils;
import nl.novi.backend.eindopdracht.hidriklandlust.dto.AccountDto;
import nl.novi.backend.eindopdracht.hidriklandlust.dto.AccountSummaryDto;
import nl.novi.backend.eindopdracht.hidriklandlust.exceptions.RecordNotFoundException;
import nl.novi.backend.eindopdracht.hidriklandlust.services.AccountService;
import nl.novi.backend.eindopdracht.hidriklandlust.services.CustomUserDetailsService;
import nl.novi.backend.eindopdracht.hidriklandlust.utils.FileStorage;
import nl.novi.backend.eindopdracht.hidriklandlust.utils.JwtUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

import static nl.novi.backend.eindopdracht.hidriklandlust.TestUtils.*;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/*@AutoConfigureMockMvc(addFilters = false)*/
@WebMvcTest(AccountController.class)
class AccountControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    AccountService accountService;

    @MockBean
    FileStorage fileStorage;

    @MockBean
    CustomUserDetailsService customUserDetailsService;

    @MockBean
    DataSource dataSource;

    @MockBean
    JwtUtil jwtUtil;

    private final String admin = "ADMIN";
    private final String user = "USER";

    @Test
    @WithMockUser(roles=admin) //For authorisation
    void getAllAccountsAuthorizedTest() throws Exception {
        List<AccountSummaryDto> dtos  = new ArrayList<>();
        AccountSummaryDto dto = generateAccountSummaryDto();
        dtos.add(dto);

        when(accountService.getAccountsSummaryDto()).thenReturn(dtos);

        mockMvc
                .perform(MockMvcRequestBuilders.get("/accounts"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].lastName", is(dto.getLastName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].firstName", is(dto.getFirstName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].employeeFunction", is(dto.getEmployeeFunction())));
    }

    @Test
    @WithMockUser(roles=user) //For authorisation
    void getAllAccountsAsUserFailsTest() throws Exception {

        mockMvc
                .perform(MockMvcRequestBuilders.get("/accounts"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    void getAllAccountsUnAuthorizedFailsTest() throws Exception {

        mockMvc
                .perform(MockMvcRequestBuilders.get("/accounts"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }


    @Test
    @WithMockUser(roles=admin) //For authorisation
    void getAccountAuthorizedTest() throws Exception {
        AccountDto dto = generateAccountDto();

        when(accountService.getAccountDto(dto.getId())).thenReturn(dto);

        mockMvc
                .perform(MockMvcRequestBuilders.get(String.format("/accounts/%s", dto.getId())))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName", is(dto.getLastName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName", is(dto.getFirstName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.employeeFunction", is(dto.getEmployeeFunction())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.streetName", is(dto.getStreetName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.city", is(dto.getCity())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.houseNumber", is(dto.getHouseNumber())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.postalCode", is(dto.getPostalCode())));
    }

    @Test
    @WithMockUser(roles=user) //For authorisation
    void getAccountUnauthorizedFailsTest() throws Exception {

        mockMvc
                .perform(MockMvcRequestBuilders.get(String.format("/accounts/%s", 1)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @WithMockUser(roles=admin) //For authorisation
    void getAccountAuthorizedAccountNotFoundTest() throws Exception {
        AccountDto dto = generateAccountDto();
        String exceptionMessage = String.format("Account with id %s does not exist.", dto.getId());

        when(accountService.getAccountDto(dto.getId())).thenThrow(new RecordNotFoundException(
                exceptionMessage));

        mockMvc
                .perform(MockMvcRequestBuilders.get(String.format("/accounts/%s", 1)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$", is(
                        exceptionMessage)));
    }

    @Test
    @WithMockUser(roles=admin) //For authorisation
    void updateAccountSucceeds() throws Exception {
        AccountSummaryDto dto = generateAccountSummaryDto();

        when(accountService.updateAccount(dto.getId(), dto)).thenReturn(dto);

        mockMvc
                .perform(
                        MockMvcRequestBuilders.put(String.format("/accounts/%s", dto.getId()))
                        .content(TestUtils.asJsonString(dto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser(roles=user) //For authorisation
    void updateAccountUnauthorizedFails() throws Exception {
        AccountSummaryDto dto = generateAccountSummaryDto();

        mockMvc
                .perform(
                        MockMvcRequestBuilders.put(String.format("/accounts/%s", dto.getId()))
                                .content(TestUtils.asJsonString(dto))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @WithMockUser(roles=admin) //For authorisation
    void updateAccountAuthorizedFailsNotFound() throws Exception {
        AccountSummaryDto dto = generateAccountSummaryDto();
        String exceptionMessage = "Test exception";

        when(accountService.updateAccount(any(Long.class), any(AccountSummaryDto.class)))
                .thenThrow(new RecordNotFoundException(exceptionMessage));

        mockMvc
                .perform(
                        MockMvcRequestBuilders
                                .put(String.format("/accounts/%s", dto.getId()))
                                .content(TestUtils.asJsonString(dto))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$", is(
                        exceptionMessage)));
    }


}
