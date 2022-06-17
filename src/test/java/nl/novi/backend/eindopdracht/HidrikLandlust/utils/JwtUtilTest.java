package nl.novi.backend.eindopdracht.HidrikLandlust.utils;


import io.jsonwebtoken.Jwts;
import nl.novi.backend.eindopdracht.HidrikLandlust.dto.UserDto;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class UserDetail implements org.springframework.security.core.userdetails.UserDetails {

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return "password";
    }

    @Override
    public String getUsername() {
        return "testUser";
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}

@SpringBootTest
public class JwtUtilTest {
    private final static String SECRET_KEY = "123456789";

    JwtUtil jwtUtil = new JwtUtil();

    UserDetail userDetails = new UserDetail();

    String token = jwtUtil.generateToken(userDetails);


    @Test
    void validateTokenIsNotExpired() {
        assertTrue(jwtUtil.validateToken(token, userDetails));
    }

    @Test
    void validateTokenIsExpired() {
        String expiredToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0VXNlciIsImV4cCI6MTY1NTM5MzI1MSwiaWF0IjoxNjU1MzkzMjUxfQ.Tjr9jK1P1BlEkRGMl2A8IQPGGESYe5CPHVqEG6cNy_4";

        assertThrows(io.jsonwebtoken.ExpiredJwtException.class, () -> jwtUtil.validateToken(expiredToken, userDetails));
    }

    @Test
    void extractUsernameFromTokenSucceeds() {
        String originalUsername = userDetails.getUsername();
        String extractedUsername = jwtUtil.extractUsername(token);
        assertEquals(originalUsername, extractedUsername);

    }
}
