package nl.novi.backend.eindopdracht.HidrikLandlust.controllers;

import nl.novi.backend.eindopdracht.HidrikLandlust.payload.AuthenticationRequest;
import nl.novi.backend.eindopdracht.HidrikLandlust.services.CustomUserDetailsService;
import nl.novi.backend.eindopdracht.HidrikLandlust.utils.FileStorage;
import nl.novi.backend.eindopdracht.HidrikLandlust.utils.JwtUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

import static nl.novi.backend.eindopdracht.HidrikLandlust.TestUtils.asJsonString;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

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

    @MockBean
    FileStorage fileStorage;

    @MockBean
    Authentication authentication;

    @Test
    void authenticationSucceedsAsAdmin() throws Exception {
        AuthenticationRequest authRequest = new AuthenticationRequest();
        authRequest.setUsername("tester");
        authRequest.setPassword("password");

        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));


        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(userDetailsService.loadUserByUsername(any(String.class)))
                .thenReturn(new org.springframework.security.core.userdetails.User("tester", "password", grantedAuthorities));
        when(jwtUtil.generateToken(any(UserDetails.class))).thenReturn("12345");

        mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/authenticate")
                        .content(asJsonString(authRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.jwt", is("12345")));

    }

    @Test
    void authenticationFailsIncorrectUsernameOrPassword() throws Exception {
        String exceptionMessage = "test";

        AuthenticationRequest authRequest = new AuthenticationRequest();
        authRequest.setUsername("tester");
        authRequest.setPassword("password");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenThrow(new BadCredentialsException(exceptionMessage));

        mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/authenticate")
                        .content(asJsonString(authRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$", is("Incorrect username or password")));

    }

    @Test
    @WithMockUser(roles = "user")
    void checkIfAuthenticatedAsUserSucceeds() throws Exception {

        mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/authenticated"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());

    }

    @Test
    @WithMockUser(roles = "admin")
    void checkIfAuthenticatedAsAdminSucceeds() throws Exception {

        mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/authenticated"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());

    }

    @Test
    void checkIfAuthenticatedUnauthenticatedFails() throws Exception {

        mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/authenticated"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isForbidden());

    }
}