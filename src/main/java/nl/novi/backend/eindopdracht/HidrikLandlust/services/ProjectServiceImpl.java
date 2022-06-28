package nl.novi.backend.eindopdracht.HidrikLandlust.services;

import nl.novi.backend.eindopdracht.HidrikLandlust.dto.AssignmentSummaryDto;
import nl.novi.backend.eindopdracht.HidrikLandlust.dto.ProjectDto;
import nl.novi.backend.eindopdracht.HidrikLandlust.dto.ProjectSummaryDto;
import nl.novi.backend.eindopdracht.HidrikLandlust.exceptions.AlreadyExistsException;
import nl.novi.backend.eindopdracht.HidrikLandlust.exceptions.DateLiesInPastException;
import nl.novi.backend.eindopdracht.HidrikLandlust.exceptions.RecordNotFoundException;
import nl.novi.backend.eindopdracht.HidrikLandlust.models.entities.Account;
import nl.novi.backend.eindopdracht.HidrikLandlust.models.entities.Assignment;
import nl.novi.backend.eindopdracht.HidrikLandlust.models.entities.Project;
import nl.novi.backend.eindopdracht.HidrikLandlust.repositories.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

@Service
public class ProjectServiceImpl implements ProjectService {
    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    AccountService accountService;

    //Autowired does not work, infinite recursion of the beans
    AssignmentService assignmentService = new AssignmentServiceImpl();


    @Override
    public List<ProjectDto> getProjects() {
        List<Project> projects = projectRepository.findAll();
        List<ProjectDto> projectDtos = new ArrayList<>();
        for (Project project: projects) {
            project.setCosts(calculateCosts(project));
            projectDtos.add(fromProject(project));
        }
        return projectDtos;
    }

    @Override
    public ProjectDto getProject(Long id) {
        Optional<Project> optionalProject = projectRepository.findById(id);
        if (optionalProject.isPresent()){

            Project project = optionalProject.get();
            project.setCosts(calculateCosts(project));
            return fromProject(project);
        } else {
            throw new RecordNotFoundException("Project not found.");
        }
    }

    @Override
    public Long getIdFromProjectCode(String code) {
        Project project = getProjectFromProjectCode(code);

        return project.getId();
    }

    @Override
    public ProjectSummaryDto createProject(ProjectDto dto) {
        if (projectCodeExists(dto)) {
            throw new AlreadyExistsException("Project with " + dto.getProjectCode() + " already exists.");
        }
        if (checkDeadlineNotInPast(dto.getDeadline())) {
            throw new DateLiesInPastException("Deadline " + dto.getDeadline() + " can not lie in the past.");
        }
        Project project = toProject(dto);

        //Initialize data
        project.setProgressPercentage((byte) 0);
        project.setCosts(0);

        Date now = new Date();
        project.setCreatedOn(new Timestamp(now.getTime()));

        project = projectRepository.save(project);
            return (fromProjectToSummary(project));
    }

    @Override
    public Long updateProject(String projectCode, ProjectDto dto) {

        Project project = getProjectFromProjectCode(projectCode);

        if (dto.getDeadline() != null) {project.setDeadline(dto.getDeadline());}
        if (dto.getBudget() != null) {project.setBudget(dto.getBudget());}
        if (dto.getDescription() != null) {project.setDescription(dto.getDescription());}
        if (dto.getProjectCode() != null) {project.setProjectCode(dto.getProjectCode());}
        if (dto.getProgressPercentage() != null) {project.setProgressPercentage(dto.getProgressPercentage());}
        projectRepository.save(project);
        return dto.getId();
    }

    @Override
    public ProjectDto addAccountToProject(String projectCode, Long accountId) {

        Project project = getProjectFromProjectCode(projectCode);
        Account account = accountService.retreiveAccount(accountId);

        if (! project.getAccounts().contains(account)) {
            project.addAccount(account);
            Project savedProject = projectRepository.save(project);
            return fromProject(savedProject);
        }
        throw new AlreadyExistsException("Account with id " + accountId + " already is member of project with code " + projectCode);

    }

    @Override
    public void deleteProject(String projectCode) {
        projectRepository.delete(getProjectFromProjectCode(projectCode));
    }

    @Override
    public Project saveProject(Project project) {
        project.setCosts(calculateCosts(project));
        return projectRepository.save(project);
    }

    @Override
    public Integer calculateCosts(Project project) {
        Integer totalCost = 0;
        for (Assignment ass: project.getAssignments()) {
            if (ass.getCosts() != null) totalCost += ass.getCosts();
        }
        return totalCost;
    }

    @Override
    public Project getProjectFromProjectCode(String projectCode){
        Optional<Project> optionalProject = projectRepository.findByProjectCode(projectCode);
        if (optionalProject.isPresent()) {
            Project project = optionalProject.get();
            project.setCosts(calculateCosts(project));
            return project;
        } else {
            throw new RecordNotFoundException("Project " + projectCode + " does not exist.");
        }
    }

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
        dto.setId(project.getId());
        dto.setCosts(project.getCosts());

        Set<AssignmentSummaryDto> assignmentSummaryDtos = new HashSet<>();
        if (project.getAssignments() != null) {
            for (Assignment assignment : project.getAssignments()) {
                AssignmentSummaryDto assignmentSummaryDto = assignmentService.fromAssignmentToSummary(assignment);
                assignmentSummaryDtos.add(assignmentSummaryDto);
            }
            dto.setAssignments(assignmentSummaryDtos);
        }


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
        proj.setCosts(dto.getCosts());

        if (dto.getAccounts() != null) {
            for (Account acc : dto.getAccounts()) {
                if (!proj.getAccounts().contains(acc)) proj.addAccount(acc);
            }
        }

        if (dto.getAssignments() != null) {
            for (AssignmentSummaryDto summaryDto : dto.getAssignments()) {
                Assignment createdAssignment = assignmentService.toAssignment(summaryDto);
                if (!proj.getAssignments().contains(createdAssignment)) {
                    proj.addAssignment(createdAssignment);
                }
            }
        }

        return proj;
    }

    @Override
    public ProjectSummaryDto fromProjectToSummary(Project project) {
        ProjectSummaryDto dto = new ProjectSummaryDto();

        dto.setProjectCode(project.getProjectCode());
        dto.setBudget(project.getBudget());
        dto.setDeadline(project.getDeadline());
        dto.setDescription(project.getDescription());
        dto.setProgressPercentage(project.getProgressPercentage());
        dto.setId(project.getId());
        dto.setCosts(project.getCosts());

        return dto;
    }

    @Override
    public Project toProject(ProjectSummaryDto dto) {
        Project proj = new Project();

        proj.setProjectCode(dto.getProjectCode());
        proj.setBudget(dto.getBudget());
        proj.setDeadline(dto.getDeadline());
        proj.setDescription(dto.getDescription());
        proj.setProgressPercentage(dto.getProgressPercentage());
        proj.setId(dto.getId());
        proj.setCosts(dto.getCosts());

        return proj;
    }
}
