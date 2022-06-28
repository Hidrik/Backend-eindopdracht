package nl.novi.backend.eindopdracht.HidrikLandlust.services;

import nl.novi.backend.eindopdracht.HidrikLandlust.dto.AssignmentDto;
import nl.novi.backend.eindopdracht.HidrikLandlust.dto.ProjectDto;
import nl.novi.backend.eindopdracht.HidrikLandlust.dto.ProjectSummaryDto;
import nl.novi.backend.eindopdracht.HidrikLandlust.models.entities.Assignment;
import nl.novi.backend.eindopdracht.HidrikLandlust.models.entities.Project;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public interface ProjectService {

    List<ProjectDto> getProjects();

    ProjectDto getProject(Long id);

    Long getIdFromProjectCode(String code);

    ProjectSummaryDto createProject(ProjectDto dto);

    Long updateProject(String projectCode, ProjectDto dto);

    Project saveProject(Project project);

    void deleteProject(String projectCode);

    Integer calculateCosts(Project project);

    ProjectDto addAccountToProject(String projectCode, Long accountId);

    Project getProjectFromProjectCode(String projectCode);

    boolean projectCodeExists(ProjectDto dto);

    boolean checkDeadlineNotInPast(LocalDate date);

    ProjectDto fromProject(Project project);

    ProjectSummaryDto fromProjectToSummary(Project project);

    Project toProject(ProjectDto dto);

    Project toProject(ProjectSummaryDto dto);


}
