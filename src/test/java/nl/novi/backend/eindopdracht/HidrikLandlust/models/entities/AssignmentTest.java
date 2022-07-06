package nl.novi.backend.eindopdracht.HidrikLandlust.models.entities;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class AssignmentTest {
    @Test
    void allAssignmentGettersAndSettersAreCorrect() {
        Assignment ass = new Assignment();
        String descriptionFinishedWork = "test1";
        String description = "test2";
        Short hoursWorked = 100;
        Integer budget = 1000;
        LocalDate deadline = new Date(System.currentTimeMillis()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        Byte progressPercentage = 50;
        Long id = 1L;


        ass.setDescriptionFinishedWork(descriptionFinishedWork);
        ass.setDescription(description);
        ass.setHoursWorked(hoursWorked);
        ass.setBudget(budget);
        ass.setDeadline(deadline);
        ass.setProgressPercentage(progressPercentage);
        ass.setId(id);

        assertEquals(ass.getDescriptionFinishedWork(), descriptionFinishedWork);
        assertEquals(ass.getHoursWorked(), hoursWorked);
        assertEquals(ass.getId(), id);
        assertEquals(ass.getDescription(), description);
        assertEquals(ass.getBudget(), budget);
        assertEquals(ass.getDeadline(), deadline);
        assertEquals(ass.getProgressPercentage(), progressPercentage);
    }
}
