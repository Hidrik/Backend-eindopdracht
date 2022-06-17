package nl.novi.backend.eindopdracht.HidrikLandlust.config;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class SpringSecurityConfigTest {

    @Test
    void typeOfPasswordEncoderIsBcrypt() {
        PasswordEncoder passEncoder = SpringSecurityConfig.passwordEncoder();
        PasswordEncoder bCryptEncoder = new BCryptPasswordEncoder();

        assertEquals(passEncoder.getClass(), bCryptEncoder.getClass());
    }
}
