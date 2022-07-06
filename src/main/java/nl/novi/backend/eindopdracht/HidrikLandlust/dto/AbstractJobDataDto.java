package nl.novi.backend.eindopdracht.HidrikLandlust.dto;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public class AbstractJobDataDto {
    private String description;

    private Byte progressPercentage;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate deadline;
    private Integer budget;

    private Integer costs;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Byte getProgressPercentage() {
        return progressPercentage;
    }

    public void setProgressPercentage(Byte progressPercentage) {
        this.progressPercentage = progressPercentage;
    }

    public LocalDate getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDate deadline) {
        this.deadline = deadline;
    }

    public Integer getBudget() {
        return budget;
    }

    public void setBudget(Integer budget) {
        this.budget = budget;
    }

    public Integer getCosts() {
        return costs;
    }

    public void setCosts(Integer costs) {
        this.costs = costs;
    }
}
