package nl.novi.backend.eindopdracht.HidrikLandlust.services;

import nl.novi.backend.eindopdracht.HidrikLandlust.dto.AssignmentDto;
import nl.novi.backend.eindopdracht.HidrikLandlust.dto.ProjectDto;
import nl.novi.backend.eindopdracht.HidrikLandlust.dto.ProjectSummaryDto;
import nl.novi.backend.eindopdracht.HidrikLandlust.models.entities.Account;
import nl.novi.backend.eindopdracht.HidrikLandlust.models.entities.Assignment;
import nl.novi.backend.eindopdracht.HidrikLandlust.models.entities.Project;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public interface ProjectService {

    List<ProjectDto> getProjectsDto();

    ProjectDto getProjectDto(Long id);

    Project getProjectFromProjectCode(String projectCode);

    ProjectSummaryDto createProject(ProjectDto dto);

    Long updateProject(String projectCode, ProjectDto dto);

    Project saveProject(Project project);

    void deleteProject(String projectCode);

    ProjectDto addAccountToProject(String projectCode, Long accountId);

    void removeAccountFromProject(String projectCode, Long accountId);

    void removeAccountFromProjects(Long accountId);

    boolean projectCodeExists(ProjectDto dto);

    boolean checkDeadlineNotInPast(LocalDate date);

    Integer calculateCosts(Project project);

    ProjectDto toProjectDto(Project project);

    ProjectSummaryDto toProjectSummaryDto(Project project);

    Project toProject(ProjectDto dto);

    Project toProject(ProjectSummaryDto dto);


}
