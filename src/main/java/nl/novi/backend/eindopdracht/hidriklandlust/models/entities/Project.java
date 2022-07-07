package nl.novi.backend.eindopdracht.hidriklandlust.models.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import nl.novi.backend.eindopdracht.hidriklandlust.models.AbstractJobData;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Project extends AbstractJobData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "project_code", nullable = false, unique = true)
    private String projectCode;


    @JsonIgnore
    @OneToMany(
            targetEntity = Assignment.class,
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.EAGER)
    private final Set<Assignment> assignments = new HashSet<>();

    @JsonIgnore
    @ManyToMany(targetEntity = Account.class)
    @JoinTable(
            name = "project_members",
            joinColumns = @JoinColumn(name = "project_id"),
            inverseJoinColumns = @JoinColumn(name = "account_id"))
    private final Set<Account> accounts = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    @Override
    public Integer getCosts() {
        Integer cost = 0;
        for (Assignment ass : assignments) {
            cost += ass.getCosts();
        }
        this.setCosts(cost);
        return cost;
    }
}
