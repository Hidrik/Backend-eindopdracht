package nl.novi.backend.eindopdracht.hidriklandlust.services;


import nl.novi.backend.eindopdracht.hidriklandlust.dto.UserDto;
import nl.novi.backend.eindopdracht.hidriklandlust.models.entities.Authority;
import nl.novi.backend.eindopdracht.hidriklandlust.models.entities.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public interface UserService {

    List<UserDto> getUsersDto();

    UserDto getUserDto(String username);

    User getUser(String username);

    UserDto createUser(UserDto userDto);

    void deleteUser(String username);

    void updateUser(String username, UserDto newUser);

    Set<Authority> getAuthorities(String username);

    void addAuthority(String username, String authority);

    void removeAuthority(String username, String authority);

    boolean authorityAlreadyExists(User user, Authority addedAuthority);

    boolean userExists(String username);

    boolean emailExists(String email);

    List<UserDto> maskPassword(List<UserDto> dtos);
    UserDto maskPassword(UserDto dto);

    UserDto toUserDto(User user);

    User toUser(UserDto userDto);

    User saveUser(User user);

}
