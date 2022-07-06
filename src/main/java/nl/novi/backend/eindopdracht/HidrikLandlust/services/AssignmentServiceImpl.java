package nl.novi.backend.eindopdracht.HidrikLandlust.services;

import nl.novi.backend.eindopdracht.HidrikLandlust.dto.AssignmentDto;
import nl.novi.backend.eindopdracht.HidrikLandlust.dto.AssignmentSummaryDto;
import nl.novi.backend.eindopdracht.HidrikLandlust.exceptions.BadRequestException;
import nl.novi.backend.eindopdracht.HidrikLandlust.exceptions.InternalFailureException;
import nl.novi.backend.eindopdracht.HidrikLandlust.exceptions.RecordNotFoundException;
import nl.novi.backend.eindopdracht.HidrikLandlust.models.entities.Account;
import nl.novi.backend.eindopdracht.HidrikLandlust.models.entities.Assignment;
import nl.novi.backend.eindopdracht.HidrikLandlust.models.entities.Component;
import nl.novi.backend.eindopdracht.HidrikLandlust.models.entities.Project;
import nl.novi.backend.eindopdracht.HidrikLandlust.repositories.AssignmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class AssignmentServiceImpl implements AssignmentService {

    @Autowired
    AssignmentRepository assignmentRepository;

    @Autowired
    ProjectService projectService;

    @Autowired
    ComponentService componentService;

    @Autowired
    AccountService accountService;

    @Override
    public Assignment getAssignment(Long id) {
        Optional<Assignment> assignmentOptional = assignmentRepository.findById(id);
        if (assignmentOptional.isPresent()) {
            return assignmentOptional.get();
        }
        throw new RecordNotFoundException("Cant find assignment with id " + id);
    }

    @Override
    public AssignmentDto getAssignmentDto(Long id) {
        return toAssignmentDto(getAssignment(id));
    }

    @Override
    public List<AssignmentDto> getAllAssignmentsDto() {
        List<AssignmentDto> dtos = new ArrayList<>();
        for (Assignment ass: assignmentRepository.findAll()){
            dtos.add(toAssignmentDto(ass));
        }
        return dtos;
    }

    @Override
    public AssignmentSummaryDto updateAssignment(Long id, AssignmentDto dto) {
        Assignment assignment = getAssignment(id);
        if (dto.getHoursWorked() != null) assignment.setHoursWorked(dto.getHoursWorked());
        if (dto.getDeadline() != null) assignment.setDeadline(dto.getDeadline());
        if (dto.getAssignmentCode() != null) assignment.setAssignmentCode(dto.getAssignmentCode());
        if (dto.getBudget() != null) assignment.setBudget(dto.getBudget());
        if (dto.getProgressPercentage() != null) assignment.setProgressPercentage(dto.getProgressPercentage());
        if (dto.getDescription() != null) assignment.setDescription(dto.getDescription());
        try {
            Assignment savedAssignment = assignmentRepository.save(assignment);
            return toAssignmentSummaryDto(savedAssignment);
        } catch (Exception e) {
            throw new InternalFailureException("Cant save assignment after updating!");
        }


    }

    @Override
    public AssignmentSummaryDto updateAssignmentFinishedWork(Long id, AssignmentSummaryDto summaryDto) {
        Assignment assignment = getAssignment(id);
        String description = generateFinishedWorkDescription(assignment, summaryDto.getDescriptionFinishedWork());

        assignment.setDescriptionFinishedWork(description);

        Short hoursWorked = (short) (assignment.getHoursWorked() + summaryDto.getHoursWorked());
        assignment.setHoursWorked(hoursWorked);

        if (summaryDto.getProgressPercentage() != null) assignment.setProgressPercentage(summaryDto.getProgressPercentage());

        Assignment savedAssignment = assignmentRepository.save(assignment);

        return toAssignmentSummaryDto(savedAssignment);
    }

    public boolean deleteAssignment(Long assignmentId) {
        try {
            Assignment ass = getAssignment(assignmentId);
            Project project = ass.getProject();
            project.removeAssignment(ass);
            projectService.saveProject(project);
            return true;

        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public AssignmentDto addAssignmentToProject(String projectCode, AssignmentDto dto) {
        dto.setHoursWorked((short) 0);
        dto.setProgressPercentage((byte) 0);
        dto.setDescriptionFinishedWork("");
        dto.setAssignmentCode(generateAssignmentCode(projectCode));

        Assignment assignment = toAssignment(dto);


        Project project = projectService.getProjectFromProjectCode(projectCode);
        project.addAssignment(assignment);

        assignment.setProject(project);
        Assignment savedAssignment = assignmentRepository.save(assignment);

        projectService.saveProject(project);

        return toAssignmentDto(savedAssignment);
    }

    @Override
    public AssignmentDto addComponentToAssignment(Integer amount, Long assignmentId, Long componentId) {
        if (amount <= 0) throw new BadRequestException(String.format("Amount can't be less than %s", amount));

        Assignment assignment = getAssignment(assignmentId);
        Integer currentAmount = assignment.getAmountOfComponentById().get(componentId);

        //Add current amount of the components
        if (currentAmount != null) amount += currentAmount;
        assignment.setAmountOfComponentById(componentId, amount);


        componentService.addComponentToAssignment(assignment, componentId, amount);

        Assignment savedAssignment = assignmentRepository.save(assignment);

        return toAssignmentDto(savedAssignment);
    }

    @Override
    public AssignmentDto removeComponentFromAssignment(Integer amount, Long assignmentId, Long componentId) {
        Assignment assignment = getAssignment(assignmentId);

        Integer currentAmount = assignment.getAmountOfComponentById().get(componentId);

        assignment.setAmountOfComponentById(componentId, currentAmount - amount);
        if (currentAmount - amount <= 0) {
            Component component = componentService.removeComponentFromAssignment(assignment, componentId, amount);
            assignment.removeComponent(component);
        }

        Assignment savedAssignment = assignmentRepository.save(assignment);

        return toAssignmentDto(savedAssignment);
    }

    @Override
    public AssignmentDto addAccountToAssignment(Long assignmentId, Long accountId) {
        Assignment assignment = getAssignment(assignmentId);
        Account account = accountService.getAccount(accountId);

        account.addAssignment(assignment);

        assignment.setAccount(account);


        Assignment savedAssignment = assignmentRepository.save(assignment);

        return toAssignmentDto(savedAssignment);
    }


    public boolean removeAccountFromAssignment(Long assignmentId, Long accountId) {

        try {
            Assignment assignment = getAssignment(assignmentId);
            assignment.setAccount(null);
            assignmentRepository.save(assignment);
            accountService.removeAssignmentFromAccount(assignment, accountId);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean assignmentCodeExists(String assignmentCode) {
        return assignmentRepository.existsByAssignmentCode(assignmentCode);
    }

    @Override
    public String generateAssignmentCode(String projectCode) {
        int i = 1;
        while(true) {
            if (!assignmentCodeExists(projectCode + "-" + i)) break;
            i ++;
        }
        return projectCode + "-" + i;
    }

    @Override
    public String generateFinishedWorkDescription(Assignment assignment, String description) {
        String oldDescription = assignment.getDescriptionFinishedWork();

        String newDescription = "";
        if (oldDescription != "") newDescription += " ||| ";

        newDescription += new Timestamp(new Date().getTime()) + ": " + description;
        return oldDescription + newDescription;
    }

    @Override
    public AssignmentDto toAssignmentDto(Assignment ass) {
        AssignmentDto dto = new AssignmentDto();
        dto.setId(ass.getId());
        dto.setHoursWorked(ass.getHoursWorked());
        dto.setDescriptionFinishedWork(ass.getDescriptionFinishedWork());
        dto.setDescription(ass.getDescription());
        dto.setBudget(ass.getBudget());
        dto.setDeadline(ass.getDeadline());
        dto.setProgressPercentage(ass.getProgressPercentage());
        dto.setCosts(ass.getCosts());
        dto.setAssignmentCode(ass.getAssignmentCode());
        dto.setAmountOfComponentById(ass.getAmountOfComponentById());

        //Could be null
        if (ass.getAccount() != null) dto.setAccount(accountService.toAccountSummaryDto(ass.getAccount()));

        //Cant be null, assignment cant exist without project
        dto.setProject(projectService.toProjectSummaryDto(ass.getProject()));

        if (ass.getComponents() != null) {
            for (Component component : ass.getComponents()) {
                dto.addComponent(componentService.toComponentSummaryDto(component));
            }
        }


        return dto;
    }

    @Override
    public AssignmentSummaryDto toAssignmentSummaryDto(Assignment ass) {
        AssignmentSummaryDto dto = new AssignmentSummaryDto();
        dto.setId(ass.getId());
        dto.setHoursWorked(ass.getHoursWorked());
        dto.setDescriptionFinishedWork(ass.getDescriptionFinishedWork());
        dto.setAssignmentCode(ass.getAssignmentCode());
        dto.setProgressPercentage(ass.getProgressPercentage());
        return dto;
    }

    @Override
    public Assignment toAssignment(AssignmentDto dto) {
        Assignment ass = new Assignment();
        ass.setId(dto.getId());
        ass.setHoursWorked(dto.getHoursWorked());
        ass.setDescriptionFinishedWork(dto.getDescriptionFinishedWork());
        ass.setDescription(dto.getDescription());
        ass.setBudget(dto.getBudget());
        ass.setDeadline(dto.getDeadline());
        ass.setProgressPercentage(dto.getProgressPercentage());
        ass.setCosts(ass.getCosts());
        ass.setAssignmentCode(dto.getAssignmentCode());
        ass.setAmountOfComponentById(dto.getAmountOfComponentById());


        if (dto.getAccount() != null) ass.setAccount(accountService.toAccount(dto.getAccount()));
        if (dto.getProject() != null) ass.setProject(projectService.toProject(dto.getProject()));

        return ass;
    }

    @Override
    public Assignment toAssignment(AssignmentSummaryDto dto) {
        Assignment ass = new Assignment();
        ass.setId(dto.getId());
        ass.setHoursWorked(dto.getHoursWorked());
        ass.setDescriptionFinishedWork(dto.getDescriptionFinishedWork());
        ass.setCosts(ass.getCosts());
        ass.setAssignmentCode(dto.getAssignmentCode());
        ass.setProgressPercentage(dto.getProgressPercentage());
        return ass;
    }
}
