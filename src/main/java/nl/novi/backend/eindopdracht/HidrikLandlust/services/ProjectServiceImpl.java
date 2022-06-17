package nl.novi.backend.eindopdracht.HidrikLandlust.services;

import nl.novi.backend.eindopdracht.HidrikLandlust.dto.ProjectDto;
import nl.novi.backend.eindopdracht.HidrikLandlust.exceptions.AlreadyExistsException;
import nl.novi.backend.eindopdracht.HidrikLandlust.exceptions.DateLiesInPastException;
import nl.novi.backend.eindopdracht.HidrikLandlust.exceptions.RecordNotFoundException;
import nl.novi.backend.eindopdracht.HidrikLandlust.models.entities.Account;
import nl.novi.backend.eindopdracht.HidrikLandlust.models.entities.Assignment;
import nl.novi.backend.eindopdracht.HidrikLandlust.models.entities.Project;
import nl.novi.backend.eindopdracht.HidrikLandlust.repositories.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ProjectServiceImpl implements ProjectService {
    @Autowired
    ProjectRepository projectRepository;

    @Override
    public List<ProjectDto> getProjects() {
        List<Project> projects = projectRepository.findAll();
        List<ProjectDto> projectDtos = new ArrayList<>();
        for (Project project: projects) {
            projectDtos.add(fromProject(project));
        }
        return projectDtos;
    }

    @Override
    public ProjectDto getProject(Long id) {
        Optional<Project> optionalProject = projectRepository.findById(id);
        if (optionalProject.isPresent()){
            return fromProject(optionalProject.get());
        } else {
            throw new RecordNotFoundException("Project not found.");
        }
    }

    @Override
    public Long getIdFromProjectCode(String code) {
        Optional<Project> optionalProject = projectRepository.findByProjectCode(code);
        if (optionalProject.isPresent()){
            return optionalProject.get().getId();
        } else {
            throw new RecordNotFoundException("Project not found.");
        }
    }

    @Override
    public Long createProject(ProjectDto dto) {
        if (projectCodeExists(dto)) {
            throw new AlreadyExistsException("Project with " + dto.getProjectCode() + " already exists.");
        }
        if (checkDeadlineNotInPast(dto.getDeadline())) {
            throw new DateLiesInPastException("Deadline " + dto.getDeadline() + " can not lie in the past.");
        }
        Project project = toProject(dto);
        project.setProgressPercentage((byte) 0);
        project = projectRepository.save(project);
            return (project.getId());
    }

    @Override
    public boolean projectCodeExists(ProjectDto dto) {
        return projectRepository.existsByProjectCode(dto.getProjectCode());
    }

    @Override
    public boolean checkDeadlineNotInPast(LocalDate date) {
        LocalDate currentDate =
                new Date(System.currentTimeMillis()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return currentDate.isAfter(date);
    }

    @Override
    public ProjectDto fromProject(Project project) {
        ProjectDto dto = new ProjectDto();

        dto.setProjectCode(project.getProjectCode());
        dto.setBudget(project.getBudget());
        dto.setDeadline(project.getDeadline());
        dto.setDescription(project.getDescription());
        dto.setProgressPercentage(project.getProgressPercentage());
        dto.setAccounts(project.getAccounts());
        dto.setAssignments(project.getAssignments());
        dto.setId(project.getId());

        return dto;
    }

    @Override
    public Project toProject(ProjectDto dto) {
        Project proj = new Project();

        proj.setProjectCode(dto.getProjectCode());
        proj.setBudget(dto.getBudget());
        proj.setDeadline(dto.getDeadline());
        proj.setDescription(dto.getDescription());
        proj.setProgressPercentage(dto.getProgressPercentage());
        proj.setId(dto.getId());

        for (Account acc : dto.getAccounts()) {
            if (!proj.getAccounts().contains(acc)) {
                proj.addAccount(acc);
            }
        }

        for (Assignment ass : dto.getAssignments()) {
            if (!proj.getAssignments().contains(ass)) {
                proj.addAssignment(ass);
            }
        }

        return proj;
    }
}
