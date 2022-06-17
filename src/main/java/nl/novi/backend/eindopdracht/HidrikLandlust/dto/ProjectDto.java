package nl.novi.backend.eindopdracht.HidrikLandlust.dto;


import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import nl.novi.backend.eindopdracht.HidrikLandlust.models.entities.Account;
import nl.novi.backend.eindopdracht.HidrikLandlust.models.entities.Assignment;

import java.util.HashSet;
import java.util.Set;

public class ProjectDto extends AbstractJobDataDto {
    private Long id;
    private String projectCode;

    @JsonSerialize
    private Set<Assignment> assignments = new HashSet<>();

    @JsonSerialize
    Set<Account> accounts = new HashSet<>();

    public String getProjectCode() {
        return projectCode;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<Assignment> getAssignments() {
        return assignments;
    }

    public void setAssignments(Set<Assignment> assignments) {
        this.assignments = assignments;
    }

    public Set<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(Set<Account> accounts) {
        this.accounts = accounts;
    }
}
