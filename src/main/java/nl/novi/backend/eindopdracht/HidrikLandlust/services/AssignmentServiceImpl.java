package nl.novi.backend.eindopdracht.HidrikLandlust.services;

import nl.novi.backend.eindopdracht.HidrikLandlust.dto.AssignmentDto;
import nl.novi.backend.eindopdracht.HidrikLandlust.dto.AssignmentSummaryDto;
import nl.novi.backend.eindopdracht.HidrikLandlust.exceptions.RecordNotFoundException;
import nl.novi.backend.eindopdracht.HidrikLandlust.models.entities.Assignment;
import nl.novi.backend.eindopdracht.HidrikLandlust.models.entities.Component;
import nl.novi.backend.eindopdracht.HidrikLandlust.models.entities.Project;
import nl.novi.backend.eindopdracht.HidrikLandlust.repositories.AssignmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AssignmentServiceImpl implements AssignmentService {

    @Autowired
    AssignmentRepository assignmentRepository;

    @Autowired
    ProjectService projectService;

    @Autowired
    ComponentService componentService;

    @Override
    public Assignment getAssignment(Long id) {
        if (assignmentRepository.findById(id).isPresent()) {
            return assignmentRepository.findById(id).get();
        }
        throw new RecordNotFoundException("Cant find assignment with id " + id);
    }

    @Override
    public AssignmentDto getAssignmentDto(Long id) {
        return fromAssignment(getAssignment(id));
    }

    @Override
    public List<AssignmentDto> getAllAssignments() {
        List<AssignmentDto> dtos = new ArrayList<>();
        for (Assignment ass: assignmentRepository.findAll()){
            dtos.add(fromAssignment(ass));
        }
        return dtos;
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

        return fromAssignment(savedAssignment);
    }

    public void removeAssignmentFromProject(Long assignmentId) {
        Assignment ass = getAssignment(assignmentId);
        Project project = ass.getProject();
        project.removeAssignment(ass);
        projectService.saveProject(project);
    }

    @Override
    public AssignmentDto addComponentToAssignment(Integer amount, Long assignmentId, Long componentId) {
        Assignment assignment = getAssignment(assignmentId);
        Integer currentAmount = assignment.getAmountOfComponentById().get(componentId);

        //Add current amount of the components
        if (currentAmount != null) amount += currentAmount;
        assignment.setAmountOfComponentById(componentId, amount);


        componentService.addComponentToAssignment(assignment, componentId);

        Assignment savedAssignment = assignmentRepository.save(assignment);

        return fromAssignment(savedAssignment);
    }

    @Override
    public void removeComponentFromAssignment(Integer amount, Long assignmentId, Long componentId) {
        Assignment assignment = getAssignment(assignmentId);

        Integer currentAmount = assignment.getAmountOfComponentById().get(componentId);

        assignment.setAmountOfComponentById(componentId, currentAmount - amount);
        if (currentAmount - amount <= 0) {
            Component component = componentService.removeComponentFromAssignment(assignment, componentId);
            assignment.removeComponent(component);
        }

        Assignment savedAssignment = assignmentRepository.save(assignment);
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
    public AssignmentDto fromAssignment(Assignment ass) {
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

        dto.setProject(projectService.fromProjectToSummary(ass.getProject()));

        if (ass.getComponents() != null) {
            for (Component component : ass.getComponents()) {
                dto.addComponent(componentService.fromComponentSummary(component));
            }
        }


        return dto;
    }

    @Override
    public AssignmentSummaryDto fromAssignmentToSummary(Assignment ass) {
        AssignmentSummaryDto dto = new AssignmentSummaryDto();
        dto.setId(ass.getId());
        dto.setHoursWorked(ass.getHoursWorked());
        dto.setDescriptionFinishedWork(ass.getDescriptionFinishedWork());
        dto.setAssignmentCode(ass.getAssignmentCode());

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

        return ass;
    }
}
