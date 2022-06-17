package nl.novi.backend.eindopdracht.HidrikLandlust.exceptions;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;


@SpringBootTest
public class ExceptionTest {
    String testString = "TEST";
    String stringTest = "TSET";

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

    @Test
    void emailAlreadyInUseExceptionGivesMessage() {
        EmailAlreadyInUseException ex = new EmailAlreadyInUseException(testString);

        assertEquals(ex.getMessage(),"Email " + testString + " already in use.");
    }

    @Test
    void userAlreadyExistsExceptionGivesMessage() {
        UserAlreadyExistsException ex = new UserAlreadyExistsException(testString);

        assertEquals(ex.getMessage(),"User " + testString + " already exists.");
    }

    @Test
    void userAlreadyHasAuthorityExceptionGivesMessage() {
        UserAlreadyHasAuthorityException ex = new UserAlreadyHasAuthorityException(testString, stringTest);

        assertEquals(ex.getMessage(),"User " + testString + " already has authority " + stringTest + ".");
    }
}
