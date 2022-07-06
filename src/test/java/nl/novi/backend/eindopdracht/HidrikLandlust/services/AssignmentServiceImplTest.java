package nl.novi.backend.eindopdracht.HidrikLandlust.services;

import nl.novi.backend.eindopdracht.HidrikLandlust.dto.AssignmentDto;
import nl.novi.backend.eindopdracht.HidrikLandlust.dto.AssignmentSummaryDto;
import nl.novi.backend.eindopdracht.HidrikLandlust.exceptions.BadRequestException;
import nl.novi.backend.eindopdracht.HidrikLandlust.exceptions.InternalFailureException;
import nl.novi.backend.eindopdracht.HidrikLandlust.exceptions.RecordNotFoundException;
import nl.novi.backend.eindopdracht.HidrikLandlust.models.entities.Assignment;
import nl.novi.backend.eindopdracht.HidrikLandlust.models.entities.Component;
import nl.novi.backend.eindopdracht.HidrikLandlust.models.entities.Project;
import nl.novi.backend.eindopdracht.HidrikLandlust.repositories.AssignmentRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static nl.novi.backend.eindopdracht.HidrikLandlust.TestUtils.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@SpringBootTest
public class AssignmentServiceImplTest {
    @Autowired
    AssignmentService assignmentService;

    @MockBean
    ComponentService componentService;

    @MockBean
    AccountService accountService;

    @MockBean
    AssignmentRepository assignmentRepository;

    @MockBean
    ProjectService projectService;

    @Test
    void getAssignmentSucceeds() {
        Assignment assignment = generateAssignment();
        Optional<Assignment> optionalAssignment = Optional.of(assignment);

        when(assignmentRepository.findById(any(Long.class))).thenReturn(optionalAssignment);

        assertEquals(assignmentService.getAssignment(assignment.getId()), assignment);
    }

    @Test
    void getAssignmentThrowsRecordNotFoundException() {
        Assignment assignment = generateAssignment();
        Optional<Assignment> optionalAssignment = Optional.ofNullable(null);

        when(assignmentRepository.findById(any(Long.class))).thenReturn(optionalAssignment);

        assertThrows(RecordNotFoundException.class, () -> assignmentService.getAssignment(assignment.getId()));
    }

    @Test
    void getAssignmentDtoSucceeds() {
        Assignment assignment = generateAssignment();
        AssignmentDto dto = generateAssignmentDto();

        Optional<Assignment> optionalAssignment = Optional.of(assignment);

        when(assignmentRepository.findById(any(Long.class))).thenReturn(optionalAssignment);

        assertThat(assignmentService.getAssignmentDto(assignment.getId()))
                .usingRecursiveComparison()
                .ignoringActualNullFields()
                .isEqualTo(dto);
    }

    @Test
    void getAllAssignmentDtoSucceeds() {
        List<Assignment> assignments = new ArrayList<>();
        Assignment assignment = generateAssignment();
        assignments.add(assignment);

        List<AssignmentDto> dtos = new ArrayList<>();
        AssignmentDto dto = generateAssignmentDto();
        dtos.add(dto);

        when(assignmentRepository.findAll()).thenReturn(assignments);

        assertThat(assignmentService.getAllAssignmentsDto())
                .usingRecursiveComparison()
                .ignoringActualNullFields()
                .isEqualTo(dtos);

    }

    @Test
    void updateAssignmentSucceeds() {
        Assignment assignment = generateAssignment();
        Optional<Assignment> optionalAssignment = Optional.of(assignment);

        AssignmentDto dto = generateAssignmentDto();
        AssignmentSummaryDto summaryDto = generateAssignmentSummaryDto();


        when(assignmentRepository.findById(any(Long.class))).thenReturn(optionalAssignment);
        when(assignmentRepository.save(any(Assignment.class))).thenReturn(assignment);

        assertThat(assignmentService.updateAssignment(dto.getId(), dto))
                .usingRecursiveComparison()
                .isEqualTo(summaryDto);

    }

    @Test
    void updateAssignmentThrowsInternalFailureException() {
        Assignment assignment = generateAssignment();
        Optional<Assignment> optionalAssignment = Optional.of(assignment);

        AssignmentDto dto = generateAssignmentDto();

        when(assignmentRepository.findById(any(Long.class))).thenReturn(optionalAssignment);
        when(assignmentRepository.save(any(Assignment.class))).thenThrow(new RuntimeException());

        assertThrows(InternalFailureException.class, () -> assignmentService.updateAssignment(dto.getId(), dto));
    }

    @Test
    void updateAssignmentFinishedWorkSucceeds() {
        Assignment assignment = generateAssignment();
        assignment.setDescriptionFinishedWork("Test 1 2 3");
        Optional<Assignment> optionalAssignment = Optional.of(assignment);
        AssignmentSummaryDto dto = generateAssignmentSummaryDto();
        dto.setDescriptionFinishedWork("Test 4 5 6");

        when(assignmentRepository.findById(any(Long.class))).thenReturn(optionalAssignment);
        when(assignmentRepository.save(any(Assignment.class))).thenReturn(assignment);


        AssignmentSummaryDto returnedDto = assignmentService.updateAssignmentFinishedWork(dto.getId(), dto);
        assertThat(returnedDto)
                .usingRecursiveComparison()
                .ignoringFields("descriptionFinishedWork", "hoursWorked")
                .isEqualTo(dto);
        assertThat(returnedDto.getDescriptionFinishedWork())
                .contains(" ||| ")
                .contains(dto.getDescriptionFinishedWork())
                .contains(assignment.getDescriptionFinishedWork());
        assertEquals(returnedDto.getHoursWorked(), assignment.getHoursWorked());
    }

    @Test
    void updateAssignmentFinishedWorkThrowsBadRequestHoursWorkedZeroOrLess() {
        AssignmentSummaryDto dto = generateAssignmentSummaryDto();
        dto.setDescriptionFinishedWork("Test 4 5 6");
        dto.setHoursWorked((short) 0);

        assertThrows(BadRequestException.class, () -> assignmentService.updateAssignmentFinishedWork(dto.getId(), dto));
    }

    @Test
    void updateAssignmentFinishedWorkThrowsBadRequestProgressPercentageZeroOrLess() {
        AssignmentSummaryDto dto = generateAssignmentSummaryDto();
        dto.setDescriptionFinishedWork("Test 4 5 6");
        dto.setProgressPercentage((byte) 0);

        assertThrows(BadRequestException.class, () -> assignmentService.updateAssignmentFinishedWork(dto.getId(), dto));
    }

    @Test
    void updateAssignmentFinishedWorkThrowsBadRequestNoDescription() {
        AssignmentSummaryDto dto = generateAssignmentSummaryDto();
        dto.setDescriptionFinishedWork("Test 4 5 6");
        dto.setDescriptionFinishedWork("");

        assertThrows(BadRequestException.class, () -> assignmentService.updateAssignmentFinishedWork(dto.getId(), dto));
    }

    @Test
    void savingAssignmentSucceeds() {
        Assignment assignment = generateAssignment();

        when(assignmentRepository.save(any(Assignment.class))).thenReturn(assignment);

        assertThat(assignmentService.saveAssignment(assignment))
                .usingRecursiveComparison()
                .isEqualTo(assignment);

    }

    @Test
    void savingAssignmentThrowsInternalFailureException() {
        Assignment assignment = generateAssignment();

        when(assignmentRepository.save(any(Assignment.class))).thenThrow(new RuntimeException());

        assertThrows(InternalFailureException.class, () -> assignmentService.saveAssignment(assignment));
    }

    @Test
    void deleteAssignmentSucceeds() {
        Assignment assignment = generateAssignment();
        Optional<Assignment> optionalAssignment = Optional.of(assignment);

        when(assignmentRepository.findById(any(Long.class))).thenReturn(optionalAssignment);
        when(projectService.saveProject(any(Project.class))).thenReturn(assignment.getProject());

        assertDoesNotThrow(() -> assignmentService.deleteAssignment(assignment.getId()));
    }

    @Test
    void deleteAssignmentThrowsInternalFailureExceptionCantSaveProject() {
        Assignment assignment = generateAssignment();
        Optional<Assignment> optionalAssignment = Optional.of(assignment);

        when(assignmentRepository.findById(any(Long.class))).thenReturn(optionalAssignment);
        when(projectService.saveProject(any(Project.class))).thenThrow(InternalFailureException.class);

        assertThrows(InternalFailureException.class, () -> assignmentService.deleteAssignment(assignment.getId()));
    }

    @Test
    void addAssignmentToProjectSucceeds() {
        Assignment savedAssignment = generateAssignment();
        Project project = generateProject();
        AssignmentDto dtoSendToMethod = generateAssignmentDto();
        AssignmentDto dtoReturnedFromMethod = generateAssignmentDto();

        savedAssignment.setProject(project);

        when(projectService.getProjectFromProjectCode(any(String.class))).thenReturn(project);
        when(assignmentRepository.save(any(Assignment.class))).thenReturn(savedAssignment);

        assertThat(assignmentService.addAssignmentToProject(project.getProjectCode(), dtoSendToMethod))
                .usingRecursiveComparison()
                .ignoringActualNullFields()
                .isEqualTo(dtoReturnedFromMethod);
    }

    @Test
    void addComponentToAssignmentSucceeds() {
        Assignment assignment = generateAssignment();
        Optional<Assignment> optionalAssignment = Optional.of(assignment);

        Component component = generateComponent();

        AssignmentDto dtoReturnedFromMethod = generateAssignmentDto();

        Integer amount = 5;
        dtoReturnedFromMethod.setAmountOfComponentById(Collections.singletonMap(1L, amount));

        when(assignmentRepository.findById(any(Long.class))).thenReturn(optionalAssignment);
        when(componentService.addComponentToAssignment(any(Assignment.class), any(Long.class), any(Integer.class))).thenReturn(component);
        when(assignmentRepository.save(any(Assignment.class))).thenReturn(assignment);

        assertThat(assignmentService.addComponentToAssignment(amount, assignment.getId(), component.getId()))
                .usingRecursiveComparison()
                .ignoringActualNullFields()
                .isEqualTo(dtoReturnedFromMethod);
    }

    @Test
    void removeAccountFromAssignmentSucceeds() {
        Assignment assignment = generateAssignment();
        Optional<Assignment> optionalAssignment = Optional.of(assignment);

        Long accountId = assignment.getAccount().getId();

        when(assignmentRepository.findById(any(Long.class))).thenReturn(optionalAssignment);
        when(assignmentRepository.save(any(Assignment.class))).thenReturn(assignment);
        doNothing().when(accountService).removeAssignmentFromAccount(assignment, accountId);

        assertDoesNotThrow(() -> assignmentService.removeAccountFromAssignment(assignment.getId(), accountId));
    }

    @Test
    void toAssignmentFromAssignmentSummaryDtoSucceeds() {
        Assignment assignment = generateAssignment();
        AssignmentSummaryDto dto =generateAssignmentSummaryDto();

        assertThat(assignmentService.toAssignment(dto))
                .usingRecursiveComparison()
                .ignoringActualNullFields()
                .isEqualTo(assignment);
    }
}
