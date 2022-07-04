package nl.novi.backend.eindopdracht.HidrikLandlust.services;

import nl.novi.backend.eindopdracht.HidrikLandlust.dto.AssignmentDto;
import nl.novi.backend.eindopdracht.HidrikLandlust.dto.AssignmentSummaryDto;
import nl.novi.backend.eindopdracht.HidrikLandlust.dto.ProjectDto;
import nl.novi.backend.eindopdracht.HidrikLandlust.models.entities.Assignment;
import nl.novi.backend.eindopdracht.HidrikLandlust.models.entities.Component;
import nl.novi.backend.eindopdracht.HidrikLandlust.models.entities.Project;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public interface AssignmentService {

    List<AssignmentDto> getAllAssignmentsDto();

    Assignment getAssignment(Long id);

    AssignmentDto getAssignmentDto(Long id);

    AssignmentSummaryDto updateAssignment(Long id, AssignmentDto dto);

    AssignmentSummaryDto updateAssignmentFinishedWork(Long id, AssignmentSummaryDto summaryDto);

    boolean deleteAssignment(Long id);

    AssignmentDto addAssignmentToProject(String projectCode, AssignmentDto dto);

    AssignmentDto addComponentToAssignment(Integer amount, Long assignmentId, Long componentId);

    AssignmentDto removeComponentFromAssignment(Integer amount, Long assignmentId, Long componentId);

    AssignmentDto addAccountToAssignment(Long assignmentId, Long accountId);

    boolean removeAccountFromAssignment(Long assignmentId, Long accountId);

    String generateAssignmentCode(String projectCode);

    boolean assignmentCodeExists(String assignmentCode);

    AssignmentDto toAssignmentDto(Assignment ass);

    AssignmentSummaryDto toAssignmentSummaryDto(Assignment ass);

    Assignment toAssignment(AssignmentDto dto);

    Assignment toAssignment(AssignmentSummaryDto dto);
}
