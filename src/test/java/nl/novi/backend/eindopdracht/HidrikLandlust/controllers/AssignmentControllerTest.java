package nl.novi.backend.eindopdracht.HidrikLandlust.controllers;

import nl.novi.backend.eindopdracht.HidrikLandlust.TestUtils;
import nl.novi.backend.eindopdracht.HidrikLandlust.dto.AssignmentDto;
import nl.novi.backend.eindopdracht.HidrikLandlust.dto.AssignmentSummaryDto;
import nl.novi.backend.eindopdracht.HidrikLandlust.exceptions.BadRequestException;
import nl.novi.backend.eindopdracht.HidrikLandlust.exceptions.RecordNotFoundException;
import nl.novi.backend.eindopdracht.HidrikLandlust.models.entities.Assignment;
import nl.novi.backend.eindopdracht.HidrikLandlust.services.AssignmentService;
import nl.novi.backend.eindopdracht.HidrikLandlust.services.CustomUserDetailsService;
import nl.novi.backend.eindopdracht.HidrikLandlust.utils.FileStorage;
import nl.novi.backend.eindopdracht.HidrikLandlust.utils.JwtUtil;
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

import static nl.novi.backend.eindopdracht.HidrikLandlust.TestUtils.*;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;


/*@AutoConfigureMockMvc(addFilters = false)*/
@WebMvcTest(AssignmentController.class)
public class AssignmentControllerTest {

    private static class Amount {
        private Integer amount = 10;

        public Integer getAmount() {
            return amount;
        }

        public void setAmount(Integer amount) {
            this.amount = amount;
        }
    }

    @Autowired
    MockMvc mockMvc;

    @MockBean
    CustomUserDetailsService customUserDetailsService;

    @MockBean
    AssignmentService assignmentService;

    @MockBean
    DataSource dataSource;

    @MockBean
    JwtUtil jwtUtil;

    @MockBean
    FileStorage fileStorageService;

    //Easier to change name
    private final String user = "USER";
    private final String admin = "ADMIN";

    //TODO add project and account verification to tests

    @Test
    @WithMockUser(roles=admin) //For authorisation
    void getAllAssignmentsAuthorizedAdminSucceeds() throws Exception {
        List<AssignmentSummaryDto> dtos = new ArrayList<>();
        AssignmentSummaryDto dto = generateAssignmentSummaryDto();
        dtos.add(dto);

        when(assignmentService.getAllAssignmentsDto()).thenReturn(dtos);

        mockMvc
                .perform(MockMvcRequestBuilders.get("/assignments"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath(
                        "$.[0].assignmentCode", is(dto.getAssignmentCode())))
                .andExpect(MockMvcResultMatchers.jsonPath(
                        "$.[0].id", is(dto.getId().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath(
                        "$.[0].descriptionFinishedWork", is(dto.getDescriptionFinishedWork())))
                .andExpect(MockMvcResultMatchers.jsonPath(
                        "$.[0].hoursWorked", is(dto.getHoursWorked().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath(
                        "$.[0].progressPercentage", is(dto.getProgressPercentage().intValue())));
    }

    @Test
    @WithMockUser(roles=user) //For authorisation
    void getAllAssignmentsAuthorizedUserSucceeds() throws Exception {
        List<AssignmentSummaryDto> dtos = new ArrayList<>();
        AssignmentSummaryDto dto = generateAssignmentSummaryDto();
        dtos.add(dto);

        when(assignmentService.getAllAssignmentsDto()).thenReturn(dtos);

        mockMvc
                .perform(MockMvcRequestBuilders.get("/assignments"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].assignmentCode", is(dto.getAssignmentCode())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].id", is(dto.getId().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].descriptionFinishedWork", is(dto.getDescriptionFinishedWork())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].hoursWorked", is(dto.getHoursWorked().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].progressPercentage", is(dto.getProgressPercentage().intValue())));
    }

    @Test
    void getAllAssignmentsUnauthorizedFails() throws Exception {

        mockMvc
                .perform(MockMvcRequestBuilders.get("/assignments"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @WithMockUser(roles=admin) //For authorisation
    void getAssignmentAuthorizedAdminSucceeds() throws Exception {
        AssignmentDto dto = generateAssignmentDto();

        when(assignmentService.getAssignmentDto(dto.getId())).thenReturn(dto);

        mockMvc
                .perform(MockMvcRequestBuilders.get(String.format("/assignments/%s", dto.getId())))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.assignmentCode", is(dto.getAssignmentCode())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", is(dto.getId().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.amountOfComponentById", is(dto.getAmountOfComponentById())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.budget", is(dto.getBudget())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description", is(dto.getDescription())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.descriptionFinishedWork", is(dto.getDescriptionFinishedWork())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.hoursWorked", is(dto.getHoursWorked().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.progressPercentage", is(dto.getProgressPercentage().intValue())));
    }

    @Test
    @WithMockUser(roles=user) //For authorisation
    void getAssignmentAuthorizedUserSucceeds() throws Exception {
        AssignmentDto dto = generateAssignmentDto();

        when(assignmentService.getAssignmentDto(dto.getId())).thenReturn(dto);

        mockMvc
                .perform(MockMvcRequestBuilders.get(String.format("/assignments/%s", dto.getId())))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.assignmentCode", is(dto.getAssignmentCode())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", is(dto.getId().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.amountOfComponentById", is(dto.getAmountOfComponentById())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.budget", is(dto.getBudget())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description", is(dto.getDescription())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.descriptionFinishedWork", is(dto.getDescriptionFinishedWork())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.hoursWorked", is(dto.getHoursWorked().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.progressPercentage", is(dto.getProgressPercentage().intValue())));
    }

    @Test
    @WithMockUser(roles=admin) //For authorisation
    void getAssignmentAuthorizedAdminFailsNotFound() throws Exception {
        AssignmentDto dto = generateAssignmentDto();

        String exceptionMessage = "test";

        when(assignmentService.getAssignmentDto(any(Long.class))).thenThrow(new RecordNotFoundException(exceptionMessage));

        mockMvc
                .perform(MockMvcRequestBuilders.get(String.format("/assignments/%s", dto.getId())))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$", is(exceptionMessage)));
    }

    @Test
    void getAssignmentUnauthorizedFails() throws Exception {
        AssignmentDto dto = generateAssignmentDto();

        when(assignmentService.getAssignmentDto(dto.getId())).thenReturn(dto);

        mockMvc
                .perform(MockMvcRequestBuilders.get(String.format("/assignments/%s", dto.getId())))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @WithMockUser(roles = admin)
    void updateFinishedWorkStringAsAdminSucceeds() throws Exception {
        AssignmentSummaryDto dto = generateAssignmentSummaryDto();
        Assignment ass = generateAssignment();

        when(assignmentService.getAssignment(dto.getId())).thenReturn(ass);

        mockMvc
                .perform(
                    MockMvcRequestBuilders
                        .put(String.format("/assignments/finishedWork/%s", dto.getId()))
                        .content(TestUtils.asJsonString(dto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser(roles = user)
    void updateFinishedWorkStringAsUserSucceeds() throws Exception {
        AssignmentSummaryDto dto = generateAssignmentSummaryDto();
        Assignment ass = generateAssignment();

        when(assignmentService.getAssignment(dto.getId())).thenReturn(ass);

        mockMvc
                .perform(
                        MockMvcRequestBuilders
                                .put(String.format("/assignments/finishedWork/%s", dto.getId()))
                                .content(TestUtils.asJsonString(dto))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void updateFinishedWorkStringUnauthorizedFails() throws Exception {
        AssignmentSummaryDto dto = generateAssignmentSummaryDto();
        Assignment ass = generateAssignment();

        when(assignmentService.getAssignment(dto.getId())).thenReturn(ass);

        mockMvc
                .perform(
                        MockMvcRequestBuilders
                                .put(String.format("/assignments/finishedWork/%s", dto.getId()))
                                .content(TestUtils.asJsonString(dto))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @WithMockUser(roles = admin)
    void updateFinishedWorkStringAsAdminFailsAssignmentNotFound() throws Exception {
        AssignmentSummaryDto dto = generateAssignmentSummaryDto();
        String exceptionMessage = "test";

        when(assignmentService.updateAssignmentFinishedWork(any(Long.class), any(AssignmentSummaryDto.class)))
                .thenThrow(new RecordNotFoundException(exceptionMessage));

        mockMvc
                .perform(
                        MockMvcRequestBuilders
                                .put(String.format("/assignments/finishedWork/%s", dto.getId()))
                                .content(TestUtils.asJsonString(dto))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$", is(exceptionMessage)));
    }

    @Test
    @WithMockUser(roles = admin)
    void updateAssignmentAsAdminSucceeds() throws Exception {
        AssignmentDto dto = generateAssignmentDto();
        AssignmentSummaryDto summaryDto = generateAssignmentSummaryDto();

        when(assignmentService.updateAssignment(dto.getId(), dto)).thenReturn(summaryDto);

        mockMvc
                .perform(
                        MockMvcRequestBuilders
                                .put(String.format("/assignments/%s", dto.getId()))
                                .content(TestUtils.asJsonString(dto))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser(roles = user)
    void updateAssignmentAsUserSucceeds() throws Exception {
        AssignmentDto dto = generateAssignmentDto();
        AssignmentSummaryDto summaryDto = generateAssignmentSummaryDto();

        when(assignmentService.updateAssignment(dto.getId(), dto)).thenReturn(summaryDto);

        mockMvc
                .perform(
                        MockMvcRequestBuilders
                                .put(String.format("/assignments/%s", dto.getId()))
                                .content(TestUtils.asJsonString(dto))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void updateAssignmentUnauthorizedFails() throws Exception {
        AssignmentDto dto = generateAssignmentDto();
        mockMvc
                .perform(
                        MockMvcRequestBuilders
                                .put(String.format("/assignments/%s", dto.getId()))
                                .content(TestUtils.asJsonString(dto))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @WithMockUser(roles = admin)
    void updateAssignmentAsAdminFailsAssignmentNotFound() throws Exception {
        AssignmentDto dto = generateAssignmentDto();

        String exceptionMessage = "test";

        when(assignmentService.updateAssignment(any(Long.class), any(AssignmentDto.class)))
                .thenThrow(new RecordNotFoundException(exceptionMessage));
        mockMvc
                .perform(
                        MockMvcRequestBuilders
                                .put(String.format("/assignments/%s", dto.getId()))
                                .content(TestUtils.asJsonString(dto))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$", is(exceptionMessage)));
    }

    @Test
    @WithMockUser(roles = admin)
    void deleteAssignmentAsAdminSucceeds() throws Exception {
        AssignmentSummaryDto dto = generateAssignmentSummaryDto();
        Assignment ass = generateAssignment();

        when(assignmentService.getAssignment(dto.getId())).thenReturn(ass);

        mockMvc
                .perform(
                        MockMvcRequestBuilders
                                .delete(String.format("/assignments/%s", dto.getId())))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser(roles = user)
    void deleteAssignmentAsUserSucceeds() throws Exception {
        AssignmentSummaryDto dto = generateAssignmentSummaryDto();
        Assignment ass = generateAssignment();

        when(assignmentService.getAssignment(dto.getId())).thenReturn(ass);

        mockMvc
                .perform(
                        MockMvcRequestBuilders
                                .delete(String.format("/assignments/%s", dto.getId())))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void deleteAssignmentUnauthorizedFails() throws Exception {
        AssignmentSummaryDto dto = generateAssignmentSummaryDto();
        Assignment ass = generateAssignment();

        when(assignmentService.getAssignment(dto.getId())).thenReturn(ass);

        mockMvc
                .perform(
                        MockMvcRequestBuilders
                                .delete(String.format("/assignments/%s", dto.getId())))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @WithMockUser(roles = admin)
    void deleteAssignmentAsAdminFailsAssignmentNotFound() throws Exception {
        String exceptionMessage = "test";

        doThrow(new RecordNotFoundException(exceptionMessage)).when(assignmentService).deleteAssignment(any(Long.class));


        mockMvc
                .perform(
                        MockMvcRequestBuilders
                                .delete(String.format("/assignments/%s", 1L)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$", is(exceptionMessage)));
    }

    @Test
    @WithMockUser(roles = admin)
    void addAccountToAssignmentAsAdminSucceeds() throws Exception {
        Long assignmentId = generateAssignmentSummaryDto().getId();
        Long accountId = generateAccountSummaryDto().getId();
        AssignmentDto assignmentDto = generateAssignmentDto();

        when(assignmentService.addAccountToAssignment(assignmentId, accountId))
                .thenReturn(assignmentDto);

        mockMvc
                .perform(
                        MockMvcRequestBuilders
                                .put(
                                        String.format("/assignments/%s/accounts/%s", assignmentId, accountId)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser(roles = user)
    void addAccountToAssignmentAsUserSucceeds() throws Exception {
        Long assignmentId = generateAssignmentSummaryDto().getId();
        Long accountId = generateAccountSummaryDto().getId();
        AssignmentDto assignmentDto = generateAssignmentDto();

        when(assignmentService.addAccountToAssignment(assignmentId, accountId))
                .thenReturn(assignmentDto);

        mockMvc
                .perform(
                        MockMvcRequestBuilders
                                .put(
                                        String.format("/assignments/%s/accounts/%s", assignmentId, accountId)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void addAccountToAssignmentUnauthorizedFails() throws Exception {
        Long assignmentId = generateAssignmentSummaryDto().getId();
        Long accountId = generateAccountSummaryDto().getId();

        mockMvc
                .perform(
                        MockMvcRequestBuilders
                                .put(
                                        String.format("/assignments/%s/accounts/%s", assignmentId, accountId)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @WithMockUser(roles = admin)
    void addAccountToAssignmentAsAdminFailsNotFound() throws Exception {
        Long assignmentId = generateAssignmentSummaryDto().getId();
        Long accountId = generateAccountSummaryDto().getId();
        String exceptionMessage = "test";

        when(assignmentService.addAccountToAssignment(any(Long.class), any(Long.class)))
                .thenThrow(new RecordNotFoundException(exceptionMessage));

        mockMvc
                .perform(
                        MockMvcRequestBuilders
                                .put(
                                        String.format("/assignments/%s/accounts/%s", assignmentId, accountId)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$", is(exceptionMessage)));
    }

    @Test
    @WithMockUser(roles = admin)
    void deleteAccountToAssignmentAsAdminSucceeds() throws Exception {
        Long assignmentId = generateAssignmentSummaryDto().getId();
        Long accountId = generateAccountSummaryDto().getId();
        AssignmentDto assignmentDto = generateAssignmentDto();

        when(assignmentService.addAccountToAssignment(assignmentId, accountId))
                .thenReturn(assignmentDto);

        mockMvc
                .perform(
                        MockMvcRequestBuilders
                                .delete(
                                        String.format("/assignments/%s/accounts/%s", assignmentId, accountId)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser(roles = user)
    void deleteAccountToAssignmentAsUserSucceeds() throws Exception {
        Long assignmentId = generateAssignmentSummaryDto().getId();
        Long accountId = generateAccountSummaryDto().getId();
        AssignmentDto assignmentDto = generateAssignmentDto();

        when(assignmentService.addAccountToAssignment(assignmentId, accountId))
                .thenReturn(assignmentDto);

        mockMvc
                .perform(
                        MockMvcRequestBuilders
                                .delete(
                                        String.format("/assignments/%s/accounts/%s", assignmentId, accountId)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void deleteAccountToAssignmentUnauthorizedFails() throws Exception {
        Long assignmentId = generateAssignmentSummaryDto().getId();
        Long accountId = generateAccountSummaryDto().getId();

        mockMvc
                .perform(
                        MockMvcRequestBuilders
                                .delete(
                                        String.format("/assignments/%s/accounts/%s", assignmentId, accountId)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @WithMockUser(roles = admin)
    void deleteAccountToAssignmentAsAdminFailsNotFound() throws Exception {
        Long assignmentId = generateAssignmentSummaryDto().getId();
        Long accountId = generateAccountSummaryDto().getId();
        String exceptionMessage = "test";

        doThrow(new RecordNotFoundException(exceptionMessage)).when(assignmentService).removeAccountFromAssignment(any(Long.class),any(Long.class));

        mockMvc
                .perform(
                        MockMvcRequestBuilders
                                .delete(
                                        String.format("/assignments/%s/accounts/%s", assignmentId, accountId)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$", is(exceptionMessage)));
    }

    @Test
    @WithMockUser(roles = admin)
    void addComponentToAssignmentAsAdminSucceeds() throws Exception {
        Amount amountClass = new Amount();
        Integer amount = amountClass.getAmount();
        AssignmentDto dto = generateAssignmentDto();
        Long assignmentId = generateAssignmentSummaryDto().getId();
        Long componentId = generateComponentDto().getId();

        when(assignmentService.addComponentToAssignment(amount, assignmentId, componentId))
                .thenReturn(dto);

        mockMvc
                .perform(
                        MockMvcRequestBuilders
                                .put(
                                        String.format("/assignments/%s/components/%s", assignmentId, componentId))
                                .content(TestUtils.asJsonString(amountClass))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser(roles = user)
    void addComponentToAssignmentAsUserSucceeds() throws Exception {
        Amount amountClass = new Amount();
        Integer amount = amountClass.getAmount();
        AssignmentDto dto = generateAssignmentDto();
        Long assignmentId = generateAssignmentSummaryDto().getId();
        Long componentId = generateComponentDto().getId();

        when(assignmentService.addComponentToAssignment(amount, assignmentId, componentId))
                .thenReturn(dto);

        mockMvc
                .perform(
                        MockMvcRequestBuilders
                                .put(
                                        String.format("/assignments/%s/components/%s", assignmentId, componentId))
                                .content(TestUtils.asJsonString(amountClass))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void addComponentToAssignmentUnauthorizedFails() throws Exception {
        Amount amountClass = new Amount();
        Long assignmentId = generateAssignmentSummaryDto().getId();
        Long componentId = generateComponentDto().getId();

        mockMvc
                .perform(
                        MockMvcRequestBuilders
                                .put(
                                        String.format("/assignments/%s/components/%s", assignmentId, componentId))
                                .content(TestUtils.asJsonString(amountClass))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @WithMockUser(roles = admin)
    void addComponentToAssignmentAsAdminFailsNotFound() throws Exception {
        Amount amountClass = new Amount();
        Long assignmentId = generateAssignmentSummaryDto().getId();
        Long componentId = generateComponentDto().getId();

        String exceptionMessage = "test";

        when(assignmentService.addComponentToAssignment(any(Integer.class), any(Long.class), any(Long.class)))
                .thenThrow(new RecordNotFoundException(exceptionMessage));

        mockMvc
                .perform(
                        MockMvcRequestBuilders
                                .put(
                                        String.format("/assignments/%s/components/%s", assignmentId, componentId))
                                .content(TestUtils.asJsonString(amountClass))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$", is(exceptionMessage)));
    }

    @Test
    @WithMockUser(roles = admin)
    void addComponentToAssignmentAsAdminFailsNoAmountSend() throws Exception {
        Long assignmentId = generateAssignmentSummaryDto().getId();
        Long componentId = generateComponentDto().getId();

        mockMvc
                .perform(
                        MockMvcRequestBuilders
                                .put(
                                        String.format("/assignments/%s/components/%s", assignmentId, componentId)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = admin)
    void addComponentToAssignmentAsAdminFailsAmountSmallerThan1() throws Exception {
        Amount amountClass = new Amount();
        amountClass.setAmount(0);
        Long assignmentId = generateAssignmentSummaryDto().getId();
        Long componentId = generateComponentDto().getId();

        String exceptionMessage = "test";

        when(assignmentService.addComponentToAssignment(any(Integer.class), any(Long.class), any(Long.class)))
                .thenThrow(new BadRequestException(exceptionMessage));

        mockMvc
                .perform(
                        MockMvcRequestBuilders
                                .put(
                                        String.format("/assignments/%s/components/%s", assignmentId, componentId))
                                .content(TestUtils.asJsonString(amountClass))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$", is(exceptionMessage)));
    }


    @Test
    @WithMockUser(roles = admin)
    void deleteComponentToAssignmentAsAdminSucceeds() throws Exception {
        Amount amountClass = new Amount();
        Integer amount = amountClass.getAmount();
        AssignmentDto dto = generateAssignmentDto();
        Long assignmentId = generateAssignmentSummaryDto().getId();
        Long componentId = generateComponentDto().getId();

        when(assignmentService.removeComponentFromAssignment(amount, assignmentId, componentId))
                .thenReturn(dto);

        mockMvc
                .perform(
                        MockMvcRequestBuilders
                                .delete(
                                        String.format("/assignments/%s/components/%s", assignmentId, componentId))
                                .content(TestUtils.asJsonString(amountClass))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isAccepted());
    }

    @Test
    @WithMockUser(roles = user)
    void deleteComponentToAssignmentAsUserSucceeds() throws Exception {
        Amount amountClass = new Amount();
        Integer amount = amountClass.getAmount();
        AssignmentDto dto = generateAssignmentDto();
        Long assignmentId = generateAssignmentSummaryDto().getId();
        Long componentId = generateComponentDto().getId();

        when(assignmentService.removeComponentFromAssignment(amount, assignmentId, componentId))
                .thenReturn(dto);

        mockMvc
                .perform(
                        MockMvcRequestBuilders
                                .delete(
                                        String.format("/assignments/%s/components/%s", assignmentId, componentId))
                                .content(TestUtils.asJsonString(amountClass))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isAccepted());
    }

    @Test
    void deleteComponentToAssignmentUnauthorizedFails() throws Exception {
        Amount amountClass = new Amount();
        Long assignmentId = generateAssignmentSummaryDto().getId();
        Long componentId = generateComponentDto().getId();

        mockMvc
                .perform(
                        MockMvcRequestBuilders
                                .delete(
                                        String.format("/assignments/%s/components/%s", assignmentId, componentId))
                                .content(TestUtils.asJsonString(amountClass))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @WithMockUser(roles = admin)
    void deleteComponentToAssignmentAsAdminFailsNotFound() throws Exception {
        Amount amountClass = new Amount();
        Long assignmentId = generateAssignmentSummaryDto().getId();
        Long componentId = generateComponentDto().getId();

        String exceptionMessage = "test";

        when(assignmentService.removeComponentFromAssignment(any(Integer.class), any(Long.class), any(Long.class)))
                .thenThrow(new RecordNotFoundException(exceptionMessage));

        mockMvc
                .perform(
                        MockMvcRequestBuilders
                                .delete(
                                        String.format("/assignments/%s/components/%s", assignmentId, componentId))
                                .content(TestUtils.asJsonString(amountClass))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$", is(exceptionMessage)));
    }

    @Test
    @WithMockUser(roles = admin)
    void deleteComponentToAssignmentAsAdminFailsNoAmountSend() throws Exception {
        Long assignmentId = generateAssignmentSummaryDto().getId();
        Long componentId = generateComponentDto().getId();

        mockMvc
                .perform(
                        MockMvcRequestBuilders
                                .delete(
                                        String.format("/assignments/%s/components/%s", assignmentId, componentId)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = admin)
    void deleteComponentToAssignmentAsAdminFailsAmountSmallerThan1() throws Exception {
        Amount amountClass = new Amount();
        amountClass.setAmount(0);
        Long assignmentId = generateAssignmentSummaryDto().getId();
        Long componentId = generateComponentDto().getId();

        String exceptionMessage = "test";

        when(assignmentService.removeComponentFromAssignment(any(Integer.class), any(Long.class), any(Long.class)))
                .thenThrow(new BadRequestException(exceptionMessage));

        mockMvc
                .perform(
                        MockMvcRequestBuilders
                                .delete(
                                        String.format("/assignments/%s/components/%s", assignmentId, componentId))
                                .content(TestUtils.asJsonString(amountClass))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$", is(exceptionMessage)));
    }
}
