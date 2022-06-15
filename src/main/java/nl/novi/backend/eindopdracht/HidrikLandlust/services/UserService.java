package nl.novi.backend.eindopdracht.HidrikLandlust.services;


import nl.novi.backend.eindopdracht.HidrikLandlust.dto.UserDto;
import nl.novi.backend.eindopdracht.HidrikLandlust.exceptions.BadRequestException;
import nl.novi.backend.eindopdracht.HidrikLandlust.exceptions.UsernameNotFoundException;
import nl.novi.backend.eindopdracht.HidrikLandlust.models.Authority;
import nl.novi.backend.eindopdracht.HidrikLandlust.models.User;
import nl.novi.backend.eindopdracht.HidrikLandlust.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public interface UserService {

    List<UserDto> getUsers();

    UserDto getUser(String username);

    boolean userExists(String username);

    String createUser(UserDto userDto);

    void deleteUser(String username);

    void updateUser(String username, UserDto newUser);

    Set<Authority> getAuthorities(String username);

    void addAuthority(String username, String authority);

    void removeAuthority(String username, String authority);

    static UserDto fromUser(User user){

        var dto = new UserDto();

        dto.username = user.getUsername();
        dto.password = user.getPassword();
        dto.enabled = user.isEnabled();
        dto.email = user.getEmail();
        dto.authorities = user.getAuthorities();

        return dto;
    }

    User toUser(UserDto userDto);

}
