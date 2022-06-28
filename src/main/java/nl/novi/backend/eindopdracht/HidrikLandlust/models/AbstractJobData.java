package nl.novi.backend.eindopdracht.HidrikLandlust.models;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.time.LocalDate;
import java.util.Date;

@MappedSuperclass
public abstract class AbstractJobData {
    @Column(nullable = false)
    private String description;

    @Column(name = "progress_percentage", nullable = false)

    @Min(value = 0, message = "Progress can't be less than 0%")
    @Max(value = 100, message = "Progress can't be higher than 100%")
    private Byte progressPercentage;

    @Column()
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate deadline;

    @Column()
    private Integer budget;

    @Column()
    private Integer costs;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_on", insertable = false, updatable = false)
    private Date createdOn;

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

    public abstract Integer getCosts();

    public void setCosts(Integer costs) {
        this.costs = costs;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }
}
