package nl.novi.backend.eindopdracht.HidrikLandlust.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.time.LocalDate;

public class AbstractJobDataDto {
    private String description;

    private Byte progressPercentage;
    private LocalDate deadline;
    private Integer budget;

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
}
