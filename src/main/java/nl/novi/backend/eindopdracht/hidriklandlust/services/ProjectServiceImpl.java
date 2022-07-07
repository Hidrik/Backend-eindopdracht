package nl.novi.backend.eindopdracht.hidriklandlust.services;

import nl.novi.backend.eindopdracht.hidriklandlust.dto.AccountSummaryDto;
import nl.novi.backend.eindopdracht.hidriklandlust.dto.AssignmentSummaryDto;
import nl.novi.backend.eindopdracht.hidriklandlust.dto.ProjectDto;
import nl.novi.backend.eindopdracht.hidriklandlust.dto.ProjectSummaryDto;
import nl.novi.backend.eindopdracht.hidriklandlust.exceptions.AlreadyExistsException;
import nl.novi.backend.eindopdracht.hidriklandlust.exceptions.DateLiesInPastException;
import nl.novi.backend.eindopdracht.hidriklandlust.exceptions.InternalFailureException;
import nl.novi.backend.eindopdracht.hidriklandlust.exceptions.RecordNotFoundException;
import nl.novi.backend.eindopdracht.hidriklandlust.models.entities.Account;
import nl.novi.backend.eindopdracht.hidriklandlust.models.entities.Assignment;
import nl.novi.backend.eindopdracht.hidriklandlust.models.entities.Project;
import nl.novi.backend.eindopdracht.hidriklandlust.repositories.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    private final AssignmentService assignmentService = new AssignmentServiceImpl();

    @Override
    public List<ProjectSummaryDto> getProjectsDto() {
        List<Project> projects = projectRepository.findAll();
        List<ProjectSummaryDto> projectDtos = new ArrayList<>();
        for (Project project: projects) {
            project.setCosts(calculateCosts(project));
            projectDtos.add(toProjectSummaryDto(project));
        }
        return projectDtos;
    }

    @Override
    public ProjectDto getProjectDto(Long id) {
        Optional<Project> optionalProject = projectRepository.findById(id);
        if (optionalProject.isPresent()){

            Project project = optionalProject.get();
            project.setCosts(calculateCosts(project));
            return toProjectDto(project);
        } else {
            throw new RecordNotFoundException("Project not found.");
        }
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
        Project savedProject = saveProject(project);
            return (toProjectSummaryDto(savedProject));
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
            for (AccountSummaryDto accountSummaryDto : dto.getAccounts()) {
                Account acc = accountService.toAccount(accountSummaryDto);
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
    public ProjectSummaryDto toProjectSummaryDto(Project project) {
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
    public ProjectDto updateProject(String projectCode, ProjectDto dto) {

        Project project = getProjectFromProjectCode(projectCode);

        if (dto.getDeadline() != null) {project.setDeadline(dto.getDeadline());}
        if (dto.getBudget() != null) {project.setBudget(dto.getBudget());}
        if (dto.getDescription() != null) {project.setDescription(dto.getDescription());}
        if (dto.getProjectCode() != null) {project.setProjectCode(dto.getProjectCode());}
        if (dto.getProgressPercentage() != null) {project.setProgressPercentage(dto.getProgressPercentage());}
        return toProjectDto(saveProject(project));
    }

    @Override
    public ProjectDto addAccountToProject(String projectCode, Long accountId) {

        Project project = getProjectFromProjectCode(projectCode);
        Account account = accountService.getAccount(accountId);

        if (! project.getAccounts().contains(account)) {
            project.addAccount(account);
            Project savedProject = saveProject(project);
            return toProjectDto(savedProject);
        }
        throw new AlreadyExistsException("Account with id " + accountId + " already is member of project with code " + projectCode);

    }

    @Override
    public ProjectDto toProjectDto(Project project) {
        ProjectDto dto = new ProjectDto();

        dto.setProjectCode(project.getProjectCode());
        dto.setBudget(project.getBudget());
        dto.setDeadline(project.getDeadline());
        dto.setDescription(project.getDescription());
        dto.setProgressPercentage(project.getProgressPercentage());
        dto.setId(project.getId());
        dto.setCosts(project.getCosts());

        if (project.getAccounts().size() > 0) {
            Set<AccountSummaryDto> accountSummaryDtos = new HashSet<>();
            for (Account acc : project.getAccounts()) {
                accountSummaryDtos.add(accountService.toAccountSummaryDto(acc));
            }
            dto.setAccounts(accountSummaryDtos);
        }

        Set<AssignmentSummaryDto> assignmentSummaryDtos = new HashSet<>();
        if (project.getAssignments() != null) {
            for (Assignment assignment : project.getAssignments()) {
                AssignmentSummaryDto assignmentSummaryDto = assignmentService.toAssignmentSummaryDto(assignment);
                assignmentSummaryDtos.add(assignmentSummaryDto);
            }
            dto.setAssignments(assignmentSummaryDtos);
        }

        return dto;
    }

    @Override
    public void removeAccountFromProject(String projectCode, Long accountId) {
        Project project = getProjectFromProjectCode(projectCode);
        Account account = accountService.getAccount(accountId);
        if (project.getAccounts().contains(account)) {
            project.removeAccount(account);
            saveProject(project);
        } else {
            throw new RecordNotFoundException(String.format("Project with code %s does not contain account with id %s", projectCode, accountId));
        }

    }

    @Override
    public Project saveProject(Project project) {
        project.setCosts(calculateCosts(project));
        try {
            return projectRepository.save(project);
        } catch(Exception e) {
            throw new InternalFailureException("Can not save project to database");
        }
    }

    @Override
    public void removeAccountFromProjects(Long accountId) {
        Account account = accountService.getAccount(accountId);
        Set<Project> projects = account.getProjects();

        if (projects.size() > 0) {
            for (Project project : projects) {
                project.removeAccount(account);
            }
        }

        projectRepository.saveAll(projects);
    }

    @Override
    public void deleteProject(String projectCode) {
        try {
            projectRepository.delete(getProjectFromProjectCode(projectCode));
        } catch (Exception e) {
            throw new InternalFailureException("Can not delete project from database");
        }

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

    @Override
    public Integer calculateCosts(Project project) {
        Integer totalCost = 0;
        for (Assignment ass: project.getAssignments()) {
            if (ass.getCosts() != null) totalCost += ass.getCosts();
        }
        return totalCost;
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
