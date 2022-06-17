package nl.novi.backend.eindopdracht.HidrikLandlust.payload;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class AuthenticationResponseTest {
    String testJwt = "12345";
    AuthenticationResponse auth = new AuthenticationResponse(testJwt);

    @Test
    void getAuthenticationResponseSucceeds() {
        assertEquals(testJwt,auth.getJwt());
    }
}
