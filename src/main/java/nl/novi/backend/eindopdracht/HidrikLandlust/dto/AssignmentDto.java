package nl.novi.backend.eindopdracht.HidrikLandlust.dto;

import nl.novi.backend.eindopdracht.HidrikLandlust.models.entities.Component;
import nl.novi.backend.eindopdracht.HidrikLandlust.models.entities.Project;

import java.util.Set;

public class AssignmentDto extends AbstractJobDataDto {
    private Long id;
    private Short hoursWorked;
    private String descriptionFinishedWork;
    private String assignmentCode;
    Set<Component> components;
    private Project project;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Short getHoursWorked() {
        return hoursWorked;
    }

    public void setHoursWorked(Short hoursWorked) {
        this.hoursWorked = hoursWorked;
    }

    public String getDescriptionFinishedWork() {
        return descriptionFinishedWork;
    }

    public void setDescriptionFinishedWork(String descriptionFinishedWork) {
        this.descriptionFinishedWork = descriptionFinishedWork;
    }

    public Set<Component> getComponents() {
        return components;
    }

    public void setComponents(Set<Component> components) {
        this.components = components;
    }

    public String getAssignmentCode() {
        return assignmentCode;
    }

    public void setAssignmentCode(String assignmentCode) {
        this.assignmentCode = assignmentCode;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }
}

