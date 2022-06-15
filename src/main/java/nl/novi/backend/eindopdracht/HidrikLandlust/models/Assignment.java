package nl.novi.backend.eindopdracht.HidrikLandlust.models;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Set;

@Entity
public class Assignment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "hours_worked")
    private Short hoursWorked;

    @Column(name = "description_finished_work")
    private String descriptionFinishedWork;

    @Column(name = "description_assignment")
    private String descriptionAssignment;

    @Column(name = "progress_percentage")
    private Byte progressPercentage;

    @Column()
    private LocalDate deadline;

    @Column()
    private Integer budget;

    @ManyToMany
    Set<Component> components;

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

    public String getDescriptionAssignment() {
        return descriptionAssignment;
    }

    public void setDescriptionAssignment(String description) {
        this.descriptionAssignment = description;
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
