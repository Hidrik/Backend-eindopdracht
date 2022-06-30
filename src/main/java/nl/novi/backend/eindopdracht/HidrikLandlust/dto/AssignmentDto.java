package nl.novi.backend.eindopdracht.HidrikLandlust.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import nl.novi.backend.eindopdracht.HidrikLandlust.models.entities.Component;
import nl.novi.backend.eindopdracht.HidrikLandlust.models.entities.Project;

import java.util.*;

public class AssignmentDto extends AbstractJobDataDto {
    private Long id;
    private Short hoursWorked;
    private String descriptionFinishedWork;
    private String assignmentCode;

    @JsonSerialize
    Set<ComponentSummaryDto> components = new HashSet<>();

    @JsonSerialize
    private Map<Long, Integer> amountOfComponentById = new HashMap<>();

    private ProjectSummaryDto project;

    private AccountSummaryDto account;

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

    public Set<ComponentSummaryDto> getComponents() {
        return components;
    }

    public void addComponent(ComponentSummaryDto component) {
        this.components.add(component);
    }

    public void removeComponent(ComponentSummaryDto component) {
        this.components.remove(component);
    }

    public String getAssignmentCode() {
        return assignmentCode;
    }

    public void setAssignmentCode(String assignmentCode) {
        this.assignmentCode = assignmentCode;
    }

    public ProjectSummaryDto getProject() {
        return project;
    }

    public void setProject(ProjectSummaryDto project) {
        this.project = project;
    }

    public Map<Long, Integer> getAmountOfComponentById() {
        return amountOfComponentById;
    }

    public void setAmountOfComponentById(Map<Long, Integer> amountOfComponentById) {
        this.amountOfComponentById = amountOfComponentById;
    }

    public AccountSummaryDto getAccount() {
        return account;
    }

    public void setAccount(AccountSummaryDto account) {
        this.account = account;
    }
}

