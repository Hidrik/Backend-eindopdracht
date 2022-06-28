package nl.novi.backend.eindopdracht.HidrikLandlust.services;


import nl.novi.backend.eindopdracht.HidrikLandlust.dto.ProjectDto;
import nl.novi.backend.eindopdracht.HidrikLandlust.dto.UserDto;
import nl.novi.backend.eindopdracht.HidrikLandlust.models.entities.Account;
import nl.novi.backend.eindopdracht.HidrikLandlust.models.entities.Authority;
import nl.novi.backend.eindopdracht.HidrikLandlust.models.entities.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public interface UserService {

    List<UserDto> getUsers();

    UserDto getUser(String username);

    boolean userExists(String username);

    boolean emailExists(String email);

    UserDto createUser(UserDto userDto);

    void deleteUser(String username);

    void updateUser(String username, UserDto newUser);

    UserDto addAccountToProject(String projectCode, String username);

    User retreiveAccount(String username);

    Set<Authority> getAuthorities(String username);

    void addAuthority(String username, String authority);

    void removeAuthority(String username, String authority);

    boolean authorityAlreadyExists(User user, Authority addedAuthority);

    String maskPassword();

    UserDto fromUser(User user);

    User toUser(UserDto userDto);

}
