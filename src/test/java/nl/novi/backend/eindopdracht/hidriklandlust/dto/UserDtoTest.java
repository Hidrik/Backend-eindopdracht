package nl.novi.backend.eindopdracht.hidriklandlust.dto;

import nl.novi.backend.eindopdracht.hidriklandlust.models.entities.Authority;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashSet;
import java.util.Set;

import static nl.novi.backend.eindopdracht.hidriklandlust.TestUtils.generateUserDto;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
class UserDtoTest {
    private final UserDto generatedUser = generateUserDto();
    //Generate standard UserDTO, set in tests and get that data
    //Original data isn't the same as the set data

    @Test
    void getCorrectUsernameAfterSetting() {
        generatedUser.setUsername("TESTUSER");

        assertEquals( "TESTUSER", generatedUser.getUsername());
    }

    @Test
    void getCorrectPasswordAfterSetting() {
        generatedUser.setPassword("PASSWORD");
        assertEquals( "PASSWORD", generatedUser.getPassword());
    }

    @Test
    void getCorrectEnabledStateAfterSettingFalse() {
        generatedUser.setEnabled(false);
        assertFalse(generatedUser.getEnabled());
    }

    @Test
    void getCorrectEmailAfterSettingFalse() {
        generatedUser.setEmail("TEST.USER.1");
        assertEquals("TEST.USER.1", generatedUser.getEmail());
    }

    @Test
    void getCorrectAuthoritiesAfterSetting() {
        Set<Authority> authorities = new HashSet<>();
        Authority authority = new Authority();
        authority.setAuthority("ROLE_ADMIN");
        authority.setUsername(generatedUser.getUsername());
        authorities.add(authority);
        generatedUser.setAuthorities(authorities);

        assertEquals(authorities, generatedUser.getAuthorities());
    }
}
