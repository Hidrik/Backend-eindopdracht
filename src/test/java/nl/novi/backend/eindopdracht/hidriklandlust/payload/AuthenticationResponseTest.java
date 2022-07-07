package nl.novi.backend.eindopdracht.hidriklandlust.payload;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class AuthenticationResponseTest {
    private final String testJwt = "12345";
    private final AuthenticationResponse auth = new AuthenticationResponse(testJwt);

    @Test
    void getAuthenticationResponseSucceeds() {
        assertEquals(testJwt,auth.getJwt());
    }
}
