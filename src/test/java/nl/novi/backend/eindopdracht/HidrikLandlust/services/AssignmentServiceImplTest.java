package nl.novi.backend.eindopdracht.HidrikLandlust.services;

import nl.novi.backend.eindopdracht.HidrikLandlust.dto.AccountDto;
import nl.novi.backend.eindopdracht.HidrikLandlust.dto.AssignmentDto;
import nl.novi.backend.eindopdracht.HidrikLandlust.dto.AssignmentSummaryDto;
import nl.novi.backend.eindopdracht.HidrikLandlust.dto.ProjectDto;
import nl.novi.backend.eindopdracht.HidrikLandlust.exceptions.InternalFailureException;
import nl.novi.backend.eindopdracht.HidrikLandlust.exceptions.RecordNotFoundException;
import nl.novi.backend.eindopdracht.HidrikLandlust.models.entities.Account;
import nl.novi.backend.eindopdracht.HidrikLandlust.models.entities.Assignment;
import nl.novi.backend.eindopdracht.HidrikLandlust.models.entities.Project;
import nl.novi.backend.eindopdracht.HidrikLandlust.repositories.AssignmentRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static nl.novi.backend.eindopdracht.HidrikLandlust.TestUtils.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class AssignmentServiceImplTest {
    @Autowired
    AssignmentService assignmentService;

    @MockBean
    AssignmentRepository assignmentRepository;

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
                .ignoringFieldsOfTypes(Project.class, ProjectDto.class, Account.class, AccountDto.class)
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
                .ignoringFields("account", "project")
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
}
