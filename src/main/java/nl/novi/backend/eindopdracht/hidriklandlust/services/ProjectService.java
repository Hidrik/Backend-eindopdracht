package nl.novi.backend.eindopdracht.hidriklandlust.services;

import nl.novi.backend.eindopdracht.hidriklandlust.dto.ProjectDto;
import nl.novi.backend.eindopdracht.hidriklandlust.dto.ProjectSummaryDto;
import nl.novi.backend.eindopdracht.hidriklandlust.models.entities.Project;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public interface ProjectService {

    List<ProjectSummaryDto> getProjectsDto();

    ProjectDto getProjectDto(Long id);

    Project getProjectFromProjectCode(String projectCode);

    ProjectSummaryDto createProject(ProjectDto dto);

    ProjectDto updateProject(String projectCode, ProjectDto dto);

    Project saveProject(Project project);

    void deleteProject(String projectCode);

    ProjectDto addAccountToProject(String projectCode, Long accountId);

    void removeAccountFromProject(String projectCode, Long accountId);

    void removeAccountFromProjects(Long accountId);

    boolean projectCodeExists(ProjectDto dto);

    boolean deadlineNotInPast(LocalDate date);

    Integer calculateCosts(Project project);

    ProjectDto toProjectDto(Project project);

    ProjectSummaryDto toProjectSummaryDto(Project project);

    Project toProject(ProjectDto dto);

    Project toProject(ProjectSummaryDto dto);


}
