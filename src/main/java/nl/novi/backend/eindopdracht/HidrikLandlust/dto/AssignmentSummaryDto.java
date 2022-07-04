package nl.novi.backend.eindopdracht.HidrikLandlust.dto;

import nl.novi.backend.eindopdracht.HidrikLandlust.models.entities.Project;

import java.util.HashSet;
import java.util.Set;

public class AssignmentSummaryDto {

    private Long id;
    private Short hoursWorked;
    private String descriptionFinishedWork;
    private String assignmentCode;
    private Byte progressPercentage;

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

    public String getAssignmentCode() {
        return assignmentCode;
    }

    public void setAssignmentCode(String assignmentCode) {
        this.assignmentCode = assignmentCode;
    }

    public Byte getProgressPercentage() {
        return progressPercentage;
    }

    public void setProgressPercentage(Byte progressPercentage) {
        this.progressPercentage = progressPercentage;
    }
}
