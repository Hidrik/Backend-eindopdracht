package nl.novi.backend.eindopdracht.HidrikLandlust.services;

import nl.novi.backend.eindopdracht.HidrikLandlust.dto.AssignmentDto;
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

    @Override
    public Assignment getAssignment(Long id) {
        if (assignmentRepository.findById(id).isPresent()) {
            return assignmentRepository.findById(id).get();
        }
        throw new RecordNotFoundException("Cant find assignment with id " + id);
    }

    @Override
    public AssignmentDto getAssignmentDtoFromId(Long id) {
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


        Project project = projectService.retreiveProject(projectCode);
        project.addAssignment(assignment);

        assignment.setProject(project);
        saveAssignment(assignment);


        projectService.saveProject(project, assignment);

        return dto;
    }
    public void removeAssignmentFromProject(Long assignmentId) {
        Assignment ass = getAssignment(assignmentId);
        Project project = ass.getProject();

        assignmentRepository.deleteById(getAssignment(assignmentId).getId());
    }

    @Override
    public Boolean componentAlreadyExists(Component comp, Assignment ass) {
        return (ass.getComponents().contains(comp));
    }

    @Override
    public void saveAssignment(Assignment ass) {
        assignmentRepository.save(ass);
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
        dto.setComponents(ass.getComponents());
        dto.setCosts(ass.getCosts());
        dto.setAssignmentCode(ass.getAssignmentCode());
        dto.setProject(ass.getProject());


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
        ass.setProject(dto.getProject());

        if (!(dto.getComponents() == null)) {
            for (Component comp : dto.getComponents()) {
                if (!componentAlreadyExists(comp, ass)) ass.addComponent(comp);
            }
        }

        return ass;
    }
}
