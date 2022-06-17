package nl.novi.backend.eindopdracht.HidrikLandlust.models.entities;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class AccountTest {

    @Test
    void accountGettersAndSettersAreOk() {
        Account account = new Account();
        String employeeFunction = "tester";
        Long id = 1L;
        String firstName = "unit";
        String lastName = "test";

        account.setEmployeeFunction(employeeFunction);
        account.setId(id);
        account.setFirstName(firstName);
        account.setLastName(lastName);

        assertEquals(account.getEmployeeFunction(), employeeFunction);
        assertEquals(account.getId(), id);
        assertEquals(account.getFirstName(), firstName);
        assertEquals(account.getLastName(), lastName);
    }

    @Test
    void accountProjectGettersAndAddersAndRemoverOk() {
        Account account = new Account();
        Project proj = new Project();
        Set<Project> projs = new HashSet<>();
        Set<Project> emptySet = new HashSet<>();

        projs.add(proj);
        account.addProject(proj);

        assertEquals(account.getProjects(), projs);

        account.removeProject(proj);

        assertEquals(account.getProjects(), emptySet);
    }

    @Test
    void accountAssignmentsGettersAndAddersAndRemoverOk() {
        Account account = new Account();
        Assignment ass = new Assignment();
        Set<Assignment> assignments = new HashSet<>();
        Set<Assignment> emptySet = new HashSet<>();

        assignments.add(ass);
        account.addAssignment(ass);

        assertEquals(account.getAssignments(), assignments);

        account.removeAssignment(ass);

        assertEquals(account.getAssignments(), emptySet);
    }
}
