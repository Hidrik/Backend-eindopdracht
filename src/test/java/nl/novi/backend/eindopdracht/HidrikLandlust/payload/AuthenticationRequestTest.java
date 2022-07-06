package nl.novi.backend.eindopdracht.HidrikLandlust.payload;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class AuthenticationRequestTest {
    private final String username = "test";
    private final String password = "password";
    private final AuthenticationRequest auth = new AuthenticationRequest(username, password);
    private final AuthenticationRequest authDefaultConstructor = fillAuthWithDefaultConstructor();

    private AuthenticationRequest fillAuthWithDefaultConstructor() {
        AuthenticationRequest auth = new AuthenticationRequest();
        auth.setUsername(username);
        auth.setPassword(password);
        return auth;
    }

    @Test
    void getUsernameReturnsUsername() {
        assertEquals(auth.getUsername(), username);
        assertEquals(authDefaultConstructor.getUsername(), username);
    }

    @Test
    void getPasswordReturnsPassword() {
        assertEquals(auth.getPassword(), password);
        assertEquals(authDefaultConstructor.getPassword(), password);
    }
}
