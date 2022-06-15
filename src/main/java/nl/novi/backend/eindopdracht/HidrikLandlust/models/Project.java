package nl.novi.backend.eindopdracht.HidrikLandlust.models;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Project {

    @Id
    @Column(name = "project_code", nullable = false)
    private String projectCode;

    @Column(nullable = false)
    private String description;

    @Column(name = "progress_percentage")
    private Byte progressPercentage;

    @Column(nullable = false)
    private LocalDate deadline;

    @Column()
    private Integer budget;

    @OneToMany(
            targetEntity = Assignment.class,
            mappedBy = "id",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.EAGER)
    private Set<Assignment> assignments = new HashSet<>();

    public String getProjectCode() {
        return projectCode;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

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

    public Set<Assignment> getAssignments() {
        return this.assignments;
    }

    public void addAssignment(Assignment assignment) {
        this.assignments.add(assignment);
    }

    public void removeAssignment(Assignment assignment) {
        this.assignments.remove(assignment);
    }
}
