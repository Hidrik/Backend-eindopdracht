package nl.novi.backend.eindopdracht.HidrikLandlust.models.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "employee_function", nullable = false)
    private String employeeFunction;

    @Column(name = "postal_code")
    private String postalCode;

    @Column(name = "street_name")
    private String streetName;

    @Column(name = "house_number")
    private Integer houseNumber;

    @Column
    private String city;

    @JsonIgnore
    @ManyToMany(mappedBy = "accounts")
    Set<Project> projects = new HashSet<>();

    @JsonIgnore
    @OneToMany(
            targetEntity = Assignment.class,
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER)
    Set<Assignment> assignments = new HashSet<>();

    @OneToOne(mappedBy = "account")
    private User user;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdOn;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedOn;

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public Date getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(Date updatedOn) {
        this.updatedOn = updatedOn;
    }

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

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public Integer getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(Integer houseNumber) {
        this.houseNumber = houseNumber;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Set<Project> getProjects() {return this.projects;}
    public void addProject(Project project) {
        this.projects.add(project);
    }
    public void removeProject(Project project) {
        this.projects.remove(project);
    }

    public void setProjects(Set<Project> projects) {
        this.projects = projects;
    }

    public Set<Assignment> getAssignments() {return this.assignments;}
    public void addAssignment(Assignment assignment) {
        this.assignments.add(assignment);
    }
    public void removeAssignment(Assignment assignment) {
        this.assignments.remove(assignment);
    }

    public void setAssignments(Set<Assignment> assignments) {
        this.assignments = assignments;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
