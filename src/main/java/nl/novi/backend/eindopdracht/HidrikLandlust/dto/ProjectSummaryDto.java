package nl.novi.backend.eindopdracht.HidrikLandlust.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import nl.novi.backend.eindopdracht.HidrikLandlust.models.entities.Account;

import java.util.HashSet;
import java.util.Set;

public class ProjectSummaryDto extends AbstractJobDataDto {
    private Long id;
    private String projectCode;

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
}
