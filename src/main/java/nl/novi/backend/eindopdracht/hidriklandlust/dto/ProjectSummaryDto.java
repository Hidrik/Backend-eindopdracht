package nl.novi.backend.eindopdracht.hidriklandlust.dto;

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
