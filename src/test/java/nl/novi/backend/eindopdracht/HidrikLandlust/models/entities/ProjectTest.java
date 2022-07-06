package nl.novi.backend.eindopdracht.HidrikLandlust.models.entities;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class ProjectTest {
    @Test
    void projectGettersAndSettersAreOk() {
        Project proj = new Project();

        String projectCode = "12345";
        Integer budget = 1000;
        LocalDate deadline = new Date(System.currentTimeMillis()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        String description = "test";
        Byte progressPercentage = 50;

        proj.setProjectCode(projectCode);
        proj.setBudget(budget);
        proj.setDeadline(deadline);
        proj.setDescription(description);
        proj.setProgressPercentage(progressPercentage);

        assertEquals(proj.getProjectCode(), projectCode);
        assertEquals(proj.getBudget(), budget);
        assertEquals(proj.getDeadline(), deadline);
        assertEquals(proj.getDescription(), description);
        assertEquals(proj.getProgressPercentage(), progressPercentage);
    }

    @Test
    void projectAssignmentsAddedAndRemovedAndGettersAreOk() {
        Project proj = new Project();
        Assignment ass = new Assignment();
        Set<Assignment> assignments = new HashSet<>();
        Set<Assignment> emptySet = new HashSet<>();


        assignments.add(ass);
        proj.addAssignment(ass);

        assertThat(proj.getAssignments()).usingRecursiveComparison().isEqualTo(assignments);

        proj.removeAssignment(ass);
        assertEquals(proj.getAssignments(), emptySet);

    }

    @Test
    void projectAccountAddedAndRemovedAndGettersAreOk() {
        Project proj = new Project();
        Account acc = new Account();
        acc.setFirstName("test");
        Set<Account> accounts = new HashSet<>();
        Set<Account> emptySet = new HashSet<>();


        accounts.add(acc);
        proj.addAccount(acc);

        assertThat(proj.getAccounts())
                .usingRecursiveComparison()
                .isEqualTo(accounts);

        proj.removeAccount(acc);
        assertEquals(proj.getAccounts(), emptySet);

    }
}
