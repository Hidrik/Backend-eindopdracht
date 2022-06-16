package nl.novi.backend.eindopdracht.HidrikLandlust.models.entities;

import nl.novi.backend.eindopdracht.HidrikLandlust.models.AbstractJobData;

import javax.persistence.*;
import java.util.Set;

@Entity
public class Assignment extends AbstractJobData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "hours_worked")
    private Short hoursWorked;

    @Column(name = "description_finished_work")
    private String descriptionFinishedWork;

    @ManyToMany(mappedBy = "assignments")
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

    public void addComponent(Component component) {
        components.add(component);
    }

    public void removeComponent(Component component) {
        components.remove(component);
    }
}
