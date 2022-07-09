package nl.novi.backend.eindopdracht.hidriklandlust.services;

import nl.novi.backend.eindopdracht.hidriklandlust.dto.AssignmentDto;
import nl.novi.backend.eindopdracht.hidriklandlust.dto.AssignmentSummaryDto;
import nl.novi.backend.eindopdracht.hidriklandlust.exceptions.BadRequestException;
import nl.novi.backend.eindopdracht.hidriklandlust.exceptions.InternalFailureException;
import nl.novi.backend.eindopdracht.hidriklandlust.exceptions.RecordNotFoundException;
import nl.novi.backend.eindopdracht.hidriklandlust.models.entities.Account;
import nl.novi.backend.eindopdracht.hidriklandlust.models.entities.Assignment;
import nl.novi.backend.eindopdracht.hidriklandlust.models.entities.Component;
import nl.novi.backend.eindopdracht.hidriklandlust.models.entities.Project;
import nl.novi.backend.eindopdracht.hidriklandlust.repositories.AssignmentRepository;
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
    public AssignmentDto getAssignmentDto(Long id) {
        return toAssignmentDto(getAssignment(id));
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

        return toAssignmentSummaryDto(saveAssignment(assignment));
    }

    @Override
    public AssignmentSummaryDto updateAssignmentFinishedWork(Long id, AssignmentSummaryDto summaryDto) {
        if (summaryDto.getHoursWorked() == null || summaryDto.getHoursWorked() <= 0) {
            throw new BadRequestException("Hours worked can not be 0");
        }
        if (summaryDto.getProgressPercentage() == null || summaryDto.getProgressPercentage() <= 0) {
            throw new BadRequestException("Progress percentage can not be 0");
        }
        if (summaryDto.getDescriptionFinishedWork() == null || summaryDto.getDescriptionFinishedWork().length() <= 0) {
            throw new BadRequestException("Must add description of finished work");
        }

        Assignment assignment = getAssignment(id);

        assignment.setDescriptionFinishedWork(
                generateFinishedWorkDescription(
                        assignment.getDescriptionFinishedWork(),
                        summaryDto.getDescriptionFinishedWork()));

        assignment.setHoursWorked(
                (short) (assignment.getHoursWorked() + summaryDto.getHoursWorked()));

        assignment.setProgressPercentage(
                summaryDto.getProgressPercentage());

        return toAssignmentSummaryDto(saveAssignment(assignment));
    }

    @Override
    public String generateFinishedWorkDescription(String oldDescription, String descriptionToAdd) {

        String newDescription = "";
        if (!oldDescription.equalsIgnoreCase("")) newDescription += " ||| ";

        newDescription += new Timestamp(new Date().getTime()) + ": " + descriptionToAdd;
        return oldDescription + newDescription;
    }

    public void deleteAssignment(Long assignmentId) {
        //External because of exception throws. getAssignment could throw RecordNotFoundException and if this is in
        // the try-catch block, it would always throw InternalFailureException.
        Assignment assignment = getAssignment(assignmentId);
        Project project = projectService.getProjectFromProjectCode(assignment.getProject().getProjectCode());
        project.removeAssignment(assignment);
        projectService.saveProject(project);
        try {
            assignmentRepository.delete(assignment);
        } catch (Exception e) {
            throw new InternalFailureException(String.format("Can not delete assignment %s", assignmentId));
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
        Assignment savedAssignment = saveAssignment(assignment);

        projectService.saveProject(project);

        return toAssignmentDto(savedAssignment);
    }

    @Override
    public String generateAssignmentCode(String projectCode) {
        int i = 0;
        while (true) {
            i++;
            if (!assignmentCodeExists(projectCode + "-" + i)) break;
        }
        return projectCode + "-" + i;
    }

    @Override
    public boolean assignmentCodeExists(String assignmentCode) {
        return assignmentRepository.existsByAssignmentCode(assignmentCode);
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
    public AssignmentDto addComponentToAssignment(Integer amount, Long assignmentId, Long componentId) {
        if (amount <= 0) throw new BadRequestException(String.format("Amount can't be less than %s", amount));

        Assignment assignment = getAssignment(assignmentId);
        Integer currentAmount = assignment.getAmountOfComponentById().get(componentId);

        //Add current amount of the components
        if (currentAmount != null) amount += currentAmount;
        assignment.setAmountOfComponentById(componentId, amount);

        componentService.addComponentToAssignment(assignment, componentId, amount);

        Assignment savedAssignment = saveAssignment(assignment);

        return toAssignmentDto(savedAssignment);
    }

    @Override
    public AssignmentDto removeComponentFromAssignment(Integer amount, Long assignmentId, Long componentId) {
        Assignment assignment = getAssignment(assignmentId);

        Integer currentAmount = assignment.getAmountOfComponentById().get(componentId);
        if (currentAmount == null)
            throw new BadRequestException(String.format("Assignment %s has no component %s", assignmentId, componentId));
        assignment.setAmountOfComponentById(componentId, currentAmount - amount);
        if (currentAmount - amount <= 0) {
            Component component = componentService.removeComponentFromAssignment(assignment, componentId, amount);
            assignment.removeComponent(component);
        }

        Assignment savedAssignment = saveAssignment(assignment);

        return toAssignmentDto(savedAssignment);
    }

    @Override
    public AssignmentDto addAccountToAssignment(Long assignmentId, Long accountId) {
        Assignment assignment = getAssignment(assignmentId);
        if (assignment.getAccount() != null)
            throw new BadRequestException(String.format("Assignment %s already has an user assigned!", assignmentId));
        Account account = accountService.getAccount(accountId);

        account.addAssignment(assignment);

        assignment.setAccount(account);

        Assignment savedAssignment = saveAssignment(assignment);

        return toAssignmentDto(savedAssignment);
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

        //Can't be null, assignment can't exist without project
        dto.setProject(projectService.toProjectSummaryDto(ass.getProject()));

        if (ass.getComponents() != null) {
            for (Component component : ass.getComponents()) {
                dto.addComponent(componentService.toComponentSummaryDto(component));
            }
        }

        return dto;
    }

    @Override
    public List<AssignmentSummaryDto> getAllAssignmentsDto() {
        List<AssignmentSummaryDto> dtos = new ArrayList<>();
        for (Assignment ass : assignmentRepository.findAll()) {
            dtos.add(toAssignmentSummaryDto(ass));
        }
        return dtos;
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

    public void removeAccountFromAssignment(Long assignmentId, Long accountId) {
        Assignment assignment = getAssignment(assignmentId);
        assignment.setAccount(null);
        saveAssignment(assignment);
        accountService.removeAssignmentFromAccount(assignment, accountId);
    }

    @Override
    public Assignment getAssignment(Long id) {
        Optional<Assignment> assignmentOptional = assignmentRepository.findById(id);
        if (assignmentOptional.isPresent()) {
            return assignmentOptional.get();
        }
        throw new RecordNotFoundException("Cant find assignment with id " + id);
    }

    @Override
    public Assignment saveAssignment(Assignment assignment) {
        try {
            return assignmentRepository.save(assignment);
        } catch (Exception e) {
            throw new InternalFailureException("Cant save assignment after updating!");
        }
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
