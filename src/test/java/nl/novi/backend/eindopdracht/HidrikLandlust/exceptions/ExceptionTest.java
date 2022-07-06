package nl.novi.backend.eindopdracht.HidrikLandlust.exceptions;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;


@SpringBootTest
public class ExceptionTest {
    private final String testString = "TEST";

    @Test
    void badRequestExceptionGivesMessage() {
        BadRequestException ex = new BadRequestException(testString);

        assertEquals(ex.getMessage(),testString);
    }

    @Test
    void badRequestExceptionGivesNoMessage() {
        BadRequestException ex = new BadRequestException();

        assertNull(ex.getMessage());
    }

    @Test
    void recordNotFoundExceptionGivesMessage() {
        RecordNotFoundException ex = new RecordNotFoundException(testString);

        assertEquals(ex.getMessage(),testString);
    }

    @Test
    void recordNotFoundExceptionGivesNoMessage() {
        RecordNotFoundException ex = new RecordNotFoundException();

        assertNull(ex.getMessage());
    }
}
