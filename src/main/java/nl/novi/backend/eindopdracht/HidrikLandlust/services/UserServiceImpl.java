package nl.novi.backend.eindopdracht.HidrikLandlust.services;


import nl.novi.backend.eindopdracht.HidrikLandlust.dto.UserDto;
import nl.novi.backend.eindopdracht.HidrikLandlust.exceptions.*;
import nl.novi.backend.eindopdracht.HidrikLandlust.models.entities.Account;
import nl.novi.backend.eindopdracht.HidrikLandlust.models.entities.Assignment;
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
    AccountService accountService;

    @Autowired
    ProjectService projectService;

    @Autowired
    AssignmentService assignmentService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<UserDto> getUsersDto() {
        List<UserDto> collection = new ArrayList<>();
        List<User> list = userRepository.findAll();
        for (User user : list) {
            collection.add(toUserDto(user));
        }
        return collection;
    }

    public UserDto getUserDto(String username) {

        User user = getUser(username);
        return toUserDto(user);
    }

    @Override
    public User getUser(String username) {
        if (!userExists(username)) throw new RecordNotFoundException("User " + username + " does not exists.");
        return userRepository.findById(username).get();
    }

    public boolean userExists(String username) {
        return userRepository.existsById(username);
    }

    public boolean emailExists(String email) {
        return userRepository.existsByEmail(email);
    }

    public UserDto createUser(UserDto userDto) {

        String username = userDto.getUsername();
        String email = userDto.getEmail();

        if (userExists(username)) throw new AlreadyExistsException(String.format("User %s already exists!", username));
        if (emailExists(email)) throw new AlreadyExistsException(String.format("Email %s already exists!", email));
        if (userDto.getAccount() == null) throw new BadRequestException("No account details were added!");

        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
        User newUser = userRepository.save(toUser(userDto));
        return toUserDto(newUser);
    }

    public void deleteUser(String username) {
        User user = getUser(username);
        Account account = user.getAccount();

        projectService.removeAccountFromProjects(account.getId());

        if (account.getAssignments() != null) {
            for (Assignment ass : account.getAssignments()) {
                assignmentService.removeAccountFromAssignment(ass.getId(), account.getId());
            }
        }

        userRepository.deleteById(username);
    }

    public void updateUser(String username, UserDto newUser) {
        User user = getUser(username);
        String newPassword = newUser.getPassword();
        String newEmail = newUser.getEmail();
        String newUsername = newUser.getUsername();

        if (newPassword != null) user.setPassword(passwordEncoder.encode(newPassword));
        if (newEmail != null) {
            if (emailExists(newEmail)) throw new AlreadyExistsException(String.format("Email %s already exists!", newEmail));
            user.setEmail(newEmail);
        }
        if (newUsername != null) {
            if (!userExists(newUsername)) throw new AlreadyExistsException(String.format("Username %s already exists!", newUsername));
            user.setUsername(newUser.getUsername());
        }

        userRepository.save(user);
    }

    public Set<Authority> getAuthorities(String username) {
        User user = getUser(username);
        UserDto userDto = toUserDto(user);
        return userDto.getAuthorities();
    }

    public void addAuthority(String username, String authority) {
        User user = getUser(username);
        Authority addAuthority = new Authority(username, authority);

        if (authorityAlreadyExists(user, addAuthority)) throw new AlreadyExistsException(String.format("User %s already has authority %s", username, authority));
        user.addAuthority(addAuthority);
        userRepository.save(user);
    }

    public void removeAuthority(String username, String authority) {
        User user = getUser(username);
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

    public String maskPassword() {
        return "**********";
    }

    public UserDto toUserDto(User user){

        var dto = new UserDto();

        dto.setUsername(user.getUsername());
        dto.setPassword(user.getPassword());
        dto.setEnabled(user.isEnabled());
        dto.setEmail(user.getEmail());
        dto.setAuthorities(user.getAuthorities());
        if (user.getAccount() != null) dto.setAccount(accountService.toAccountSummaryDto(user.getAccount()));

        return dto;
    }

    public User toUser(UserDto userDto){

        var user = new User();

        user.setUsername(userDto.getUsername());
        user.setPassword(userDto.getPassword());
        user.setEnabled(userDto.getEnabled());
        user.setEmail(userDto.getEmail());

        if (userDto.getAccount() == null) return user;

        user.setAccount(accountService.toAccount(userDto.getAccount()));

        return user;
    }

}
