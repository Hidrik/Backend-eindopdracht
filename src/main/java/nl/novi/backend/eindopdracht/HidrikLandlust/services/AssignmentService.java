package nl.novi.backend.eindopdracht.HidrikLandlust.services;

import nl.novi.backend.eindopdracht.HidrikLandlust.dto.AssignmentDto;
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

    AssignmentDto getAssignmentDtoFromId(Long id);

    AssignmentDto addAssignmentToProject(String projectCode, AssignmentDto dto);

    void removeAssignmentFromProject(Long id);

    Boolean componentAlreadyExists(Component comp, Assignment ass);

    void saveAssignment(Assignment ass);

    boolean assignmentCodeExists(String assignmentCode);

    String generateAssignmentCode(String projectCode);

    AssignmentDto fromAssignment(Assignment ass);

    Assignment toAssignment(AssignmentDto dto);
}
