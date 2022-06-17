package nl.novi.backend.eindopdracht.HidrikLandlust.models.entities;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class UserTest {
    @Test
    void userGettersAndSettersOk() {
        User user = new User();
        String username = "test";
        String password = "password";
        Boolean enabled = true;
        String email = "test@test.test";

        user.setUsername(username);
        user.setPassword(password);
        user.setEnabled(enabled);
        user.setEmail(email);

        assertEquals(user.getUsername(), username);
        assertEquals(user.getEmail(), email);
        assertEquals(user.getPassword(), password);
        assertTrue(user.isEnabled());
    }

    @Test
    void userAuthoritiesAdderAndGetterAndRemoverAreOk() {
        User user = new User();
        Authority auth = new Authority("test", "ROLE_USER");
        Set<Authority> authorities = new HashSet<>();
        Set<Authority> emptySet = new HashSet<>();


        authorities.add(auth);
        user.addAuthority(auth);

        assertEquals(user.getAuthorities(), authorities);

        user.removeAuthority(auth);
        assertEquals(user.getAuthorities(), emptySet);

    }
}
