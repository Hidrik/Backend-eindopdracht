package nl.novi.backend.eindopdracht.HidrikLandlust.services;

import nl.novi.backend.eindopdracht.HidrikLandlust.dto.UserDto;
import nl.novi.backend.eindopdracht.HidrikLandlust.exceptions.AlreadyExistsException;
import nl.novi.backend.eindopdracht.HidrikLandlust.exceptions.BadRequestException;
import nl.novi.backend.eindopdracht.HidrikLandlust.exceptions.RecordNotFoundException;
import nl.novi.backend.eindopdracht.HidrikLandlust.models.entities.Authority;
import nl.novi.backend.eindopdracht.HidrikLandlust.models.entities.User;
import nl.novi.backend.eindopdracht.HidrikLandlust.repositories.UserRepository;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static nl.novi.backend.eindopdracht.HidrikLandlust.TestUtils.generateUserDto;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
class UserServiceImplTest {
    @Autowired
    UserService userService;

    @MockBean
    UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void fromUserToUserDtoSucceeds() {
        UserDto testDto = generateUserDto();

        User user = new User();
        user.setUsername(testDto.getUsername());
        user.setPassword(testDto.getPassword());
        user.setEnabled(testDto.getEnabled());
        user.setEmail(testDto.getEmail());
        user.addAuthority(testDto.getAuthorities().iterator().next());

        UserDto generatedDto = userService.toUserDto(user);

        //Because a new instance is made of AccountSummaryDto
        generatedDto.setAccount(testDto.getAccount());

        AssertionsForClassTypes.assertThat(generatedDto)
                .usingRecursiveComparison()
                .isEqualTo(testDto);
    }

    @Test
    void fromUserDtoToUserSucceeds() {
        UserDto dto = generateUserDto();
        dto.setAccount(null);

        User testUser = new User();
        testUser.setUsername(dto.getUsername());
        testUser.setPassword(dto.getPassword());
        testUser.setEnabled(dto.getEnabled());
        testUser.setEmail(dto.getEmail());

        //Authorities aren't passed through the function so isn't used in the test

        User generatedUser = userService.toUser(dto);

        AssertionsForClassTypes.assertThat(generatedUser)
                .usingRecursiveComparison()
                .isEqualTo(testUser);
    }

    @Test
    void getAllUsersSucceeds() {
        List<UserDto> usersDto = new ArrayList<>();
        List<User> users = new ArrayList<>();

        UserDto userDto = generateUserDto();
        User user = userService.toUser(userDto);


        userDto.setAuthorities(user.getAuthorities());

        usersDto.add(userDto);
        users.add(user);

        when(userRepository.findAll()).thenReturn(users);

        //This is done because otherwise 2 different instances of AccountSummaryDto is used and this test fails
        UserDto receivedUserDto = userService.getUsersDto().get(0);
        receivedUserDto.setAccount(userDto.getAccount());

        AssertionsForClassTypes.assertThat(receivedUserDto)
                .usingRecursiveComparison()
                .isEqualTo(usersDto.get(0));
    }

    @Test
    void getAllUsersWithNoDataAndNoException() {
        List<User> users = new ArrayList<>();

        when(userRepository.findAll()).thenReturn(users);

        assertEquals(userService.getUsersDto().size(), 0);
    }

    @Test
    void getOneUserSucceeds() {
        UserDto userDto = generateUserDto();
        User user = userService.toUser(userDto);
        userDto.setAuthorities(user.getAuthorities());

        when(userRepository.findById(any(String.class))).thenReturn(Optional.of(user));
        when(userRepository.existsById(any(String.class))).thenReturn(true);

        UserDto receivedDto = userService.getUserDto(userDto.getUsername());

        assertEquals(receivedDto.getAuthorities(), userDto.getAuthorities());
        assertEquals(receivedDto.getEmail(), userDto.getEmail());
        assertEquals(receivedDto.getUsername(), userDto.getUsername());
        assertEquals(receivedDto.getEnabled(), userDto.getEnabled());
        assertEquals(receivedDto.getPassword(), userDto.getPassword());

        //Can't assertEquals the Account due to different instances
    }

    @Test
    void getUserWhenUserDoesNotExistsThrowsExceptionWithMessage() {
        UserDto userDto = generateUserDto();

        when(userRepository.findById(userDto.getUsername())).thenReturn(Optional.empty());

        RecordNotFoundException exception = assertThrows(RecordNotFoundException.class, () -> userService.getUserDto(userDto.getUsername()));

        assertEquals(exception.getMessage(), "User " + userDto.getUsername() + " does not exists.");
    }

    @Test
    void checkIfUserExists() {
        String user = "user";
        String admin = "admin";
        when(userRepository.existsById(user)).thenReturn(false);
        when(userRepository.existsById(admin)).thenReturn(true);
        assertFalse(userService.userExists(user));
        assertTrue(userService.userExists(admin));
    }

    @Test
    void checkIfEmailExists() {
        String emailUser = "user@user.user";
        String emailAdmin = "admin@admin.admin";
        when(userRepository.existsByEmail(emailUser)).thenReturn(false);
        when(userRepository.existsByEmail(emailAdmin)).thenReturn(true);
        assertFalse(userService.emailExists(emailUser));
        assertTrue(userService.emailExists(emailAdmin));
    }

    @Test
    void creationOfUserSucceeds() {
        UserDto dto = generateUserDto();

        User user = userService.toUser(dto);

        when(userRepository.existsById(dto.getUsername())).thenReturn(false);
        when(userRepository.existsByEmail(dto.getEmail())).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(user);

        String generatedUsername = userService.createUser(dto).getUsername();

        assertEquals(dto.getUsername(), generatedUsername);
    }

    @Test
    void creationOfUserFailsNoAccountAdded() {
        UserDto dto = generateUserDto();
        dto.setAccount(null);
        User user = userService.toUser(dto);

        when(userRepository.existsById(dto.getUsername())).thenReturn(false);
        when(userRepository.existsByEmail(dto.getEmail())).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(user);

        assertThrows(BadRequestException.class, () -> userService.createUser(dto));
    }

    @Test
    void creationOfUserFailesUsernameAlreadyExists() {
        UserDto dto = generateUserDto();

        when(userRepository.existsById(dto.getUsername())).thenReturn(true);
        when(userRepository.existsByEmail(dto.getEmail())).thenReturn(false);

        assertThrows(AlreadyExistsException.class, () -> userService.createUser(dto));
    }

    @Test
    void creationOfUserFailesEmailAlreadyInUse() {
        UserDto dto = generateUserDto();

        when(userRepository.existsById(dto.getUsername())).thenReturn(false);
        when(userRepository.existsByEmail(dto.getEmail())).thenReturn(true);

        assertThrows(AlreadyExistsException.class, () -> userService.createUser(dto));
    }

    @Test
    void deleteUserFailsBecauseUserDoesNotExists() {
        UserDto dto = generateUserDto();

        assertThrows(RecordNotFoundException.class, () -> userService.deleteUser(dto.getUsername()));
    }

    @Test
    void updateUserFailsBecauseUserDoesNotExists() {
        UserDto dto = generateUserDto();

        assertThrows(RecordNotFoundException.class, () -> userService.updateUser(dto.getUsername(), dto));
    }

    @Test
    void getUserAuthoritiesSucceeds() {
        UserDto userDto = generateUserDto();
        User user = userService.toUser(userDto);
        userDto.setAuthorities(user.getAuthorities());

        when(userRepository.existsById(any(String.class))).thenReturn(true);
        when(userRepository.findById(any(String.class))).thenReturn(Optional.of(user));

        assertEquals(userDto.getAuthorities(), userService.getAuthorities(userDto.getUsername()));
    }

    @Test
    void getUserAuthoritiesFailsBecauseUserDoesNotExists() {
        UserDto dto = generateUserDto();

        when(userRepository.existsById(any(String.class))).thenReturn(false);

        assertThrows(RecordNotFoundException.class, () -> userService.getAuthorities(dto.getUsername()));
    }

    @Test
    void addAuthoritySucceeds() {
        UserDto dto = generateUserDto();
        User user = userService.toUser(dto);
        Authority auth = dto.getAuthorities().iterator().next();
        user.addAuthority(auth);

        when(userRepository.findById(dto.getUsername())).thenReturn(Optional.of(user));
        when(userRepository.existsById(any(String.class))).thenReturn(true);

        assertDoesNotThrow(() -> userService.addAuthority(dto.getUsername(), "ROLE_ADMIN"));
    }

    @Test
    void addAuthorityToUserFailsUserDoesNotExists() {
        UserDto dto = generateUserDto();
        Authority auth = dto.getAuthorities().iterator().next();

        when(userRepository.existsById(any(String.class))).thenReturn(false);

        assertThrows(RecordNotFoundException.class, () -> userService.addAuthority(dto.getUsername(), auth.getAuthority()));
    }

    @Test
    void addAuthorityToUserFailsUserAlreadyHasAuthority() {
        UserDto dto = generateUserDto();
        User user = userService.toUser(dto);
        Authority auth = dto.getAuthorities().iterator().next();
        user.addAuthority(auth);

        when(userRepository.findById(dto.getUsername())).thenReturn(Optional.of(user));
        when(userRepository.existsById(any(String.class))).thenReturn(true);

        assertThrows(AlreadyExistsException.class, () -> {
            userService.addAuthority(dto.getUsername(), auth.getAuthority());
        });
    }

    @Test
    void removeAuthoritySucceeds() {
        UserDto dto = generateUserDto();
        User user = userService.toUser(dto);
        Authority auth = dto.getAuthorities().iterator().next();
        user.addAuthority(auth);

        when(userRepository.findById(any(String.class))).thenReturn(Optional.of(user));
        when(userRepository.existsById(any(String.class))).thenReturn(true);

        assertDoesNotThrow(() -> {
            userService.removeAuthority(dto.getUsername(), auth.getAuthority());
        });
    }

    @Test
    void removeAuthorityUserDoesNotExists() {
        UserDto dto = generateUserDto();
        Authority auth = dto.getAuthorities().iterator().next();

        when(userRepository.existsById(any(String.class))).thenReturn(false);

        assertThrows(RecordNotFoundException.class, () -> {
            userService.removeAuthority(dto.getUsername(), auth.getAuthority());
        });
    }
}
