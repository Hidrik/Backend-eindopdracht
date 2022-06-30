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

    List<AssignmentDto> getAllAssignments();

    Assignment getAssignment(Long id);

    AssignmentDto getAssignmentDto(Long id);

    void updateAssignment(Long id, AssignmentDto dto);

    void updateAssignmentFinishedWork(Long id, AssignmentSummaryDto summaryDto);

    void deleteAssignment(Long id);

    AssignmentDto addAssignmentToProject(String projectCode, AssignmentDto dto);

    AssignmentDto addComponentToAssignment(Integer amount, Long assignmentId, Long componentId);

    void removeComponentFromAssignment(Integer amount, Long assignmentId, Long componentId);

    void addAccountToAssignment(Long assignmentId, Long accountId);

    void removeAccountFromAssignment(Long assignmentId, Long accountId);

    String generateAssignmentCode(String projectCode);

    boolean assignmentCodeExists(String assignmentCode);

    AssignmentDto toAssignmentDto(Assignment ass);

    AssignmentSummaryDto toAssignmentSummaryDto(Assignment ass);

    Assignment toAssignment(AssignmentDto dto);

    Assignment toAssignment(AssignmentSummaryDto dto);
}
