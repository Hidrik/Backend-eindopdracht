package nl.novi.backend.eindopdracht.HidrikLandlust.models.entities;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "employee_function")
    private String employeeFunction;

    @ManyToMany(mappedBy = "accounts")
    Set<Project> projects;

    @OneToMany(
            targetEntity = Assignment.class,
            mappedBy = "id",
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER)
    Set<Assignment> assignments = new HashSet<>();

    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmployeeFunction() {
        return employeeFunction;
    }
    public void setEmployeeFunction(String employeeFunction) {
        this.employeeFunction = employeeFunction;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public Set<Project> getProjects() {return this.projects;}
    public void addProject(Project project) {
        this.projects.add(project);
    }
    public void removeProject(Project project) {
        this.projects.remove(project);
    }

    public Set<Assignment> getAssignments() {return this.assignments;}
    public void addAssignment(Assignment assignment) {
        this.assignments.add(assignment);
    }
    public void removeAssignment(Assignment assignment) {
        this.assignments.remove(assignment);
    }
}
