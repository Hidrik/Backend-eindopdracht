package nl.novi.backend.eindopdracht.HidrikLandlust.services;


import nl.novi.backend.eindopdracht.HidrikLandlust.dto.UserDto;
import nl.novi.backend.eindopdracht.HidrikLandlust.exceptions.*;
import nl.novi.backend.eindopdracht.HidrikLandlust.models.entities.Authority;
import nl.novi.backend.eindopdracht.HidrikLandlust.models.entities.User;
import nl.novi.backend.eindopdracht.HidrikLandlust.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<UserDto> getUsers() {
        List<UserDto> collection = new ArrayList<>();
        List<User> list = userRepository.findAll();
        for (User user : list) {
            collection.add(UserService.fromUser(user));
        }
        return collection;
    }

    public UserDto getUser(String username) {
        UserDto dto;
        Optional<User> user = userRepository.findById(username);
        if (user.isPresent()){
            dto = UserService.fromUser(user.get());
        }else {
            throw new RecordNotFoundException("User " + username + "does not exists.");
        }
        return dto;
    }

    public boolean userExists(String username) {
        return userRepository.existsById(username);
    }

    public boolean emailExists(String email) {
        return userRepository.existsByEmail(email);
    }

    public String createUser(UserDto userDto) {

        String username = userDto.getUsername();
        String email = userDto.getEmail();

        if (userExists(username)) throw new UserAlreadyExistsException(username);
        if (emailExists(email)) throw new EmailAlreadyInUseException(email);

        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
        User newUser = userRepository.save(UserService.toUser(userDto));
        return newUser.getUsername();
    }

    public void deleteUser(String username) {
        if (!userExists(username)) throw new RecordNotFoundException("User " + username + "does not exists.");
        userRepository.deleteById(username);
    }

    public void updateUser(String username, UserDto newUser) {
        if (!userExists(username)) throw new RecordNotFoundException("User " + username + "does not exists.");
        User user = userRepository.findById(username).get();
        user.setPassword(passwordEncoder.encode(newUser.getPassword()));
        userRepository.save(user);
    }

    public Set<Authority> getAuthorities(String username) {
        if (!userExists(username)) throw new RecordNotFoundException("User " + username + "does not exists.");
        User user = userRepository.findById(username).get();
        UserDto userDto = UserService.fromUser(user);
        return userDto.getAuthorities();
    }

    public void addAuthority(String username, String authority) {
        if (!userExists(username)) throw new RecordNotFoundException("User " + username + "does not exists.");
        User user = userRepository.findById(username).get();
        Authority addAuthority = new Authority(username, authority);

        if (authorityAlreadyExists(user, addAuthority)) throw new UserAlreadyHasAuthorityException(username, authority);
        user.addAuthority(addAuthority);
        userRepository.save(user);
    }

    public void removeAuthority(String username, String authority) {
        if (!userExists(username)) throw new RecordNotFoundException("User " + username + "does not exists.");
        User user = userRepository.findById(username).get();
        Authority authorityToRemove = user.getAuthorities().stream().filter((a) -> a.getAuthority().equalsIgnoreCase(authority)).findAny().get();
        user.removeAuthority(authorityToRemove);
        userRepository.save(user);
    }

    public boolean authorityAlreadyExists(User user, Authority addedAuthority) {
        boolean returnValue = false;
        for (Authority currentAuthority : user.getAuthorities()) {
            if (currentAuthority.getAuthority().equals(addedAuthority.getAuthority())) {
                returnValue = true;
                break;
            }
        }
        return returnValue;
    }

}
