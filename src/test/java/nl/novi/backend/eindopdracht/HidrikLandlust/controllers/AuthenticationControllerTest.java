package nl.novi.backend.eindopdracht.HidrikLandlust.controllers;

import nl.novi.backend.eindopdracht.HidrikLandlust.TestUtils;
import nl.novi.backend.eindopdracht.HidrikLandlust.dto.UserDto;
import nl.novi.backend.eindopdracht.HidrikLandlust.services.CustomUserDetailsService;
import nl.novi.backend.eindopdracht.HidrikLandlust.utils.JwtUtil;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

class TestUser {
    private String username = "testuser";
    private String password = "password";

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}


@WebMvcTest(AuthenticationController.class)
public class AuthenticationControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    JwtUtil jwtUtil;

    @MockBean
    AuthenticationManager authenticationManager;

    @MockBean
    CustomUserDetailsService userDetailsService;

    @MockBean
    DataSource dataSource;

    }