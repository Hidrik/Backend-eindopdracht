package nl.novi.backend.eindopdracht.HidrikLandlust.models.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import nl.novi.backend.eindopdracht.HidrikLandlust.models.AbstractJobData;

import javax.persistence.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Entity
public class Assignment extends AbstractJobData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "hours_worked")
    private Short hoursWorked;

    @Column(name = "description_finished_work", columnDefinition = "TEXT")
    private String descriptionFinishedWork;

    @Column(name = "assignment_code")
    private String assignmentCode;

    @JsonIgnore
    @ManyToMany(mappedBy = "assignments")
    private final Set<Component> components = new HashSet<>();

    @ElementCollection
    private Map<Long, Integer> amountOfComponentById = new HashMap<>();

    @JsonIgnore
    @ManyToOne()
    @JoinColumn(name="project_id", nullable=false)
    private Project project;

    @JsonIgnore
    @ManyToOne()
    @JoinColumn(name="account_id")
    private Account account;

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

    public Set<Component> getComponents() {
        return components;
    }

    public void addComponent(Component component) {
        components.add(component);
    }

    public void removeComponent(Component component) {
        components.remove(component);
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public String getAssignmentCode() {
        return assignmentCode;
    }

    public void setAssignmentCode(String assignmentCode) {
        this.assignmentCode = assignmentCode;
    }

    public void setAmountOfComponentById(Long id, Integer amount) {

        this.amountOfComponentById.put(id, amount);
        if (this.amountOfComponentById.get(id) <= 0) {
            this.amountOfComponentById.remove(id);
        }
    }

    public Map<Long, Integer> getAmountOfComponentById() {
        return this.amountOfComponentById;
    }

    public void setAmountOfComponentById(Map<Long, Integer> amountOfComponentById) {
        this.amountOfComponentById = amountOfComponentById;
    }

    @Override
    public Integer getCosts() {
        int cost = 0;
        if (components.size() == 0) return 0;

        for (Component comp : components) {
            Integer amountOfCurrentComponent = amountOfComponentById.get(comp.getId());
            cost += comp.getPrice() * amountOfCurrentComponent;
        }
        this.setCosts(cost);
        return cost;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }
}
