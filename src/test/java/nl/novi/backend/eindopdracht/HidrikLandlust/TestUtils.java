package nl.novi.backend.eindopdracht.HidrikLandlust;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.novi.backend.eindopdracht.HidrikLandlust.dto.UserDto;
import nl.novi.backend.eindopdracht.HidrikLandlust.models.entities.Authority;

import java.util.HashSet;
import java.util.Set;

public class TestUtils {

    public static String asJsonString(final Object obj) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static UserDto generateUserDto() {
        UserDto testUser = new UserDto();
        Set<Authority> authorities = new HashSet<>();
        Authority authority = new Authority();

        testUser.setUsername("testUser");
        testUser.setEmail("test@user.1");
        testUser.setPassword("hallo");
        testUser.setEnabled(true);

        authority.setAuthority("ROLE_USER");
        authority.setUsername(testUser.getUsername());
        authorities.add(authority);

        testUser.setAuthorities(authorities);

        return testUser;
    }
}
