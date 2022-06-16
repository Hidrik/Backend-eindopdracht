package nl.novi.backend.eindopdracht.HidrikLandlust.models.entities;

import nl.novi.backend.eindopdracht.HidrikLandlust.models.AbstractJobData;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Project extends AbstractJobData {

    @Id
    @Column(name = "project_code", nullable = false)
    private String projectCode;

    @OneToMany(
            targetEntity = Assignment.class,
            mappedBy = "id",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.EAGER)
    private Set<Assignment> assignments = new HashSet<>();

    @ManyToMany(targetEntity = Account.class)
    @JoinTable(
            name = "project_members",
            joinColumns = @JoinColumn(name = "project_id"),
            inverseJoinColumns = @JoinColumn(name = "account_id"))
    Set<Account> accounts;

    public String getProjectCode() {
        return projectCode;
    }
    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
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

    public Set<Account> getAccounts() { return this.accounts; }
    public void addAccount(Account account) {
        this.accounts.add(account);
    }
    public void removeAccount(Account account) {
        this.accounts.remove(account);
    }
}
