package nl.novi.backend.eindopdracht.hidriklandlust.controllers;
import nl.novi.backend.eindopdracht.hidriklandlust.TestUtils;
import nl.novi.backend.eindopdracht.hidriklandlust.dto.UserDto;
import nl.novi.backend.eindopdracht.hidriklandlust.exceptions.AlreadyExistsException;
import nl.novi.backend.eindopdracht.hidriklandlust.exceptions.RecordNotFoundException;
import nl.novi.backend.eindopdracht.hidriklandlust.models.entities.User;
import nl.novi.backend.eindopdracht.hidriklandlust.services.CustomUserDetailsService;
import nl.novi.backend.eindopdracht.hidriklandlust.utils.FileStorage;
import nl.novi.backend.eindopdracht.hidriklandlust.services.UserService;
import nl.novi.backend.eindopdracht.hidriklandlust.utils.JwtUtil;

import org.junit.jupiter.api.Test;

import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.sql.DataSource;
import java.util.*;

import static nl.novi.backend.eindopdracht.hidriklandlust.TestUtils.generateUserDto;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

/*@AutoConfigureMockMvc(addFilters = false)*/
@WebMvcTest(UserController.class)
class UserControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    CustomUserDetailsService customUserDetailsService;

    @MockBean
    UserService userService;

    @MockBean
    DataSource dataSource;

    @MockBean
    JwtUtil jwtUtil;

    @MockBean
    FileStorage fileStorageService;

    @Test
    @WithMockUser(roles="ADMIN") //For authorisation
    void getAllUsersAuthorizedTest() throws Exception {
        List<UserDto> users = new ArrayList<>();
        users.add(generateUserDto());

        when(userService.getUsersDto()).thenReturn(users);

        mockMvc
                .perform(MockMvcRequestBuilders.get("/users"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].username", is(generateUserDto().getUsername())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].email", is(generateUserDto().getEmail())));
        }

    @Test
    @WithMockUser()
    void getAllUsersUnauthorizedTest() throws Exception {
        List<UserDto> users = new ArrayList<>();

        users.add(generateUserDto());


        when(userService.getUsersDto()).thenReturn(users);

        mockMvc
                .perform(MockMvcRequestBuilders.get("/users"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getUserAuthorizedTest() throws Exception {

        when(userService.getUserDto(generateUserDto().getUsername())).thenReturn(generateUserDto());

        mockMvc
                .perform(MockMvcRequestBuilders.get("/users/testUser"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.username", is(generateUserDto().getUsername())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email", is(generateUserDto().getEmail())));
    }

    @Test
    @WithMockUser(roles = "USER")
    void getUserUnauthorizedTest() throws Exception {

        when(userService.getUserDto(generateUserDto().getUsername())).thenReturn(generateUserDto());

        mockMvc
                .perform(MockMvcRequestBuilders.get("/users/testUser"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void makeNewUserAsAdminSucceed() throws Exception {

        when(userService.createUser(Mockito.any(UserDto.class))).thenReturn(generateUserDto());

        mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/users")
                        .content(TestUtils.asJsonString(generateUserDto()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.username", is(generateUserDto().getUsername())));
    }

    @Test
    @WithMockUser(roles = "USER")
    void makeNewUserAsUserFailedUnauthorized() throws Exception {

        when(userService.createUser(Mockito.any(UserDto.class))).thenReturn(generateUserDto());

        mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/users")
                        .content(TestUtils.asJsonString(generateUserDto()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void makeNewUserNoDataReceived() throws Exception {

        mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/users")
                        .content("")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void makeNewUserWhenNameAlreadyExists() throws Exception {

        when(userService.createUser(Mockito.any(UserDto.class))).thenThrow(new AlreadyExistsException(
                String.format("User %s already exists!", generateUserDto().getUsername())));

        mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/users")
                        .content(TestUtils.asJsonString(generateUserDto()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isConflict())
                .andExpect(MockMvcResultMatchers.jsonPath("$", is(
                        String.format("User %s already exists!", generateUserDto().getUsername()))));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void makeNewUserWhenEmailAlreadyExists() throws Exception {

        when(userService.createUser(Mockito.any(UserDto.class))).thenThrow(
                new AlreadyExistsException(String.format("Email %s already exists!", generateUserDto().getEmail())));

        mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/users")
                        .content(TestUtils.asJsonString(generateUserDto()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isConflict())
                .andExpect(MockMvcResultMatchers.jsonPath("$", is(
                        String.format("Email %s already exists!", generateUserDto().getEmail()))));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void updateUserSucceedsAsAdmin() throws Exception {

        mockMvc
                .perform(MockMvcRequestBuilders
                        .put(String.format("/users/%s", generateUserDto().getUsername()))
                        .content(TestUtils.asJsonString(generateUserDto()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isAccepted());
    }

    @Test
    @WithMockUser(roles = "USER")
    void updateUserFailsNotAuthorized() throws Exception {

        mockMvc
                .perform(MockMvcRequestBuilders
                        .put(String.format("/users/%s", generateUserDto().getUsername()))
                        .content(TestUtils.asJsonString(generateUserDto()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteUserSucceedsAsAdmin() throws Exception {

        mockMvc
                .perform(MockMvcRequestBuilders
                        .delete(String.format("/users/%s", generateUserDto().getUsername()))
                        .content(TestUtils.asJsonString(generateUserDto()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isAccepted());
    }

    @Test
    @WithMockUser(roles = "USER")
    void deleteUserFailsNotAuthorized() throws Exception {

        mockMvc
                .perform(MockMvcRequestBuilders
                        .delete(String.format("/users/%s", generateUserDto().getUsername()))
                        .content(TestUtils.asJsonString(generateUserDto()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getAuthoritiesUserExistsAndRoleIsAdmin() throws Exception {

        when(userService.getAuthorities(generateUserDto().getUsername())).thenReturn(generateUserDto().getAuthorities());

        mockMvc
                .perform(MockMvcRequestBuilders.get(String.format("/users/%s/authorities", generateUserDto().getUsername())))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].authority", is("ROLE_USER")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].username", is(generateUserDto().getUsername())));
    }

    @Test
    @WithMockUser(roles = "USER")
    void getAuthoritiesUserExistsAndRoleIsUser() throws Exception {

        mockMvc
                .perform(MockMvcRequestBuilders.get(String.format("/users/%s/authorities", generateUserDto().getUsername())))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getAuthoritiesUserDoesntExistsAndRoleIsAdmin() throws Exception {

        when(userService.getAuthorities(generateUserDto().getUsername())).thenThrow(
                new RecordNotFoundException("user " + generateUserDto().getUsername() + "does not exists."));

        mockMvc
                .perform(MockMvcRequestBuilders.get(String.format("/users/%s/authorities", generateUserDto().getUsername())))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());
        }

    @Test
    @WithMockUser(roles = "ADMIN")
    void addAuthorityToUserAsAdminSucceeds() throws Exception {

        User user = userService.toUser(generateUserDto());

        when(userService.getUser(generateUserDto().getUsername())).thenReturn(user);

        mockMvc
                .perform(MockMvcRequestBuilders
                        .put(String.format("/users/%s/authorities", generateUserDto().getUsername()))
                        .content(TestUtils.asJsonString(generateUserDto()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isAccepted());
    }

    @Test
    @WithMockUser(roles = "USER")
    void addAuthorityToUserAsUserFailed() throws Exception {

        mockMvc
                .perform(MockMvcRequestBuilders
                        .post(String.format("/users/%s/authorities", generateUserDto().getUsername()))
                        .content(TestUtils.asJsonString(generateUserDto()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void removeAuthorityAsAdminSucceeds() throws Exception {

        class SendAuthorityData {
            String authority = "ROLE_USER";

            public String getAuthority() {return authority;}

            public void setAuthority(String authority) {
                this.authority = authority;
            }
        }

        User user = userService.toUser(generateUserDto());

        when(userService.getUser(generateUserDto().getUsername())).thenReturn(user);

        mockMvc
                .perform(MockMvcRequestBuilders
                        .delete(String.format("/users/%1$s/authorities", generateUserDto().getUsername()))
                        .content(TestUtils.asJsonString(new SendAuthorityData()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isAccepted());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void removeAuthorityAsAdminUserDoesNotExists() throws Exception {

        mockMvc
                .perform(MockMvcRequestBuilders
                        .delete(String.format("/users/%1$s/authorities/%2$s", generateUserDto().getUsername(), "ROLE_USER"))
                        .content(TestUtils.asJsonString(generateUserDto()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "USER")
    void removeAuthorityAsUser() throws Exception {

        mockMvc
                .perform(MockMvcRequestBuilders
                        .delete(String.format("/users/%1$s/authorities/%2$s", generateUserDto().getUsername(), "ROLE_USER"))
                        .content(TestUtils.asJsonString(generateUserDto()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }
}
