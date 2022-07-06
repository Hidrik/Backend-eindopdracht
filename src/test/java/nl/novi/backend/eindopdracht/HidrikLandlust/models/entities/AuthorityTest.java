package nl.novi.backend.eindopdracht.HidrikLandlust.models.entities;

import org.junit.jupiter.api.Test;
import org.springframework.stereotype.Service;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Service
public class AuthorityTest {

    @Test
    void AuthorityAllGettersAndSettersAndConstructorsAreOk() {
        String usernameDefaultConstructor = "testDefaultConstructor";
        String authorityStringDefaultConstructor = "authorityDefaultConstructor";
        String username = "test";
        String authorityString = "authority";

        Authority authDefaultConstructor = new Authority();
        Authority auth = new Authority(username, authorityString);

        authDefaultConstructor.setUsername(usernameDefaultConstructor);
        authDefaultConstructor.setAuthority(authorityStringDefaultConstructor);

        assertEquals(authDefaultConstructor.getUsername(), usernameDefaultConstructor);
        assertEquals(authDefaultConstructor.getAuthority(), authorityStringDefaultConstructor);
        assertEquals(auth.getUsername(), username);
        assertEquals(auth.getAuthority(), authorityString);

    }
}
