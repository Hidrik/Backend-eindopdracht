package nl.novi.backend.eindopdracht.hidriklandlust.services;

import nl.novi.backend.eindopdracht.hidriklandlust.dto.AssignmentDto;
import nl.novi.backend.eindopdracht.hidriklandlust.dto.AssignmentSummaryDto;
import nl.novi.backend.eindopdracht.hidriklandlust.models.entities.Assignment;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AssignmentService {

    List<AssignmentSummaryDto> getAllAssignmentsDto();

    Assignment getAssignment(Long id);

    AssignmentDto getAssignmentDto(Long id);

    AssignmentSummaryDto updateAssignment(Long id, AssignmentDto dto);

    AssignmentSummaryDto updateAssignmentFinishedWork(Long id, AssignmentSummaryDto summaryDto);

    Assignment saveAssignment(Assignment assignment);

    void deleteAssignment(Long id);

    AssignmentDto addAssignmentToProject(String projectCode, AssignmentDto dto);

    AssignmentDto addComponentToAssignment(Integer amount, Long assignmentId, Long componentId);

    AssignmentDto removeComponentFromAssignment(Integer amount, Long assignmentId, Long componentId);

    AssignmentDto addAccountToAssignment(Long assignmentId, Long accountId);

    void removeAccountFromAssignment(Long assignmentId, Long accountId);

    String generateAssignmentCode(String projectCode);

    String generateFinishedWorkDescription(String oldDescription, String descriptionToAdd);

    boolean assignmentCodeExists(String assignmentCode);

    AssignmentDto toAssignmentDto(Assignment ass);

    AssignmentSummaryDto toAssignmentSummaryDto(Assignment ass);

    Assignment toAssignment(AssignmentDto dto);

    Assignment toAssignment(AssignmentSummaryDto dto);
}
