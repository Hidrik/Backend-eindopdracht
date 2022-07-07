package nl.novi.backend.eindopdracht.HidrikLandlust;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.novi.backend.eindopdracht.HidrikLandlust.dto.*;
import nl.novi.backend.eindopdracht.HidrikLandlust.models.entities.*;
import org.springframework.security.core.parameters.P;

import java.time.LocalDate;
import java.util.*;

public class TestUtils {

    public static String asJsonString(final Object obj) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static UserDto generateUserDto() {
        UserDto testUser = new UserDto();
        Set<Authority> authorities = new HashSet<>();

        testUser.setUsername("testUser");
        testUser.setEmail("test@user.1");
        testUser.setPassword("hallo");
        testUser.setEnabled(true);

        Authority authority = new Authority();
        authority.setAuthority("ROLE_USER");
        authority.setUsername(testUser.getUsername());
        authorities.add(authority);
        testUser.setAuthorities(authorities);

        AccountSummaryDto accountSummaryDto = new AccountSummaryDto();
        accountSummaryDto.setFirstName("test");
        accountSummaryDto.setLastName("er");
        accountSummaryDto.setEmployeeFunction("Tester");
        accountSummaryDto.setId(1L);
        testUser.setAccount(accountSummaryDto);

        return testUser;
    }

    public static Account generateAccount() {
        Account account = new Account();
        account.setStreetName("testlaan");
        account.setLastName("er");
        account.setFirstName("test");
        account.setId(1L);
        account.setEmployeeFunction("Tester");
        account.setCity("testcity");
        account.setHouseNumber(2);
        account.setUpdatedOn(new Date());
        account.setCreatedOn(new Date(100));
        account.setPostalCode("test-ER");

        Set<Assignment> assignments = new HashSet<>();
        Assignment assignment = generateAssignment();
        assignments.add(assignment);
        account.setAssignments(assignments);

        Set<Project> projects = new HashSet<>();
        Project project = generateProject();
        projects.add(project);
        account.setProjects(projects);

        return account;
    }

    public static AccountDto generateAccountDto() {
        AccountDto dto = new AccountDto();
        dto.setStreetName("testlaan");
        dto.setCity("testcity");
        dto.setHouseNumber(2);
        dto.setPostalCode("test-ER");
        dto.setFirstName("test");
        dto.setLastName("er");
        dto.setEmployeeFunction("Tester");
        dto.setId(1L);

        Set<ProjectSummaryDto> projects = new HashSet<>();
        ProjectSummaryDto project = generateProjectSummaryDto();
        projects.add(project);
        dto.setProjects(projects);

        Set<Assignment> assignments = new HashSet<>();
        Assignment assignment = generateAssignment();
        assignments.add(assignment);
        dto.setAssignments(assignments);

        return dto;
    }

    private static ProjectSummaryDto generateProjectSummaryDto() {
        ProjectSummaryDto dto = new ProjectSummaryDto();
        dto.setId(1L);
        dto.setBudget(1000);
        dto.setCosts(100);
        dto.setProgressPercentage((byte) 50);
        dto.setDeadline(LocalDate.of(2022, 12, 31));
        dto.setProjectCode("test-test");
        dto.setDescription("test test test test");
        return dto;
    }

    public static Project generateProject() {
        Project project = new Project();
        project.setProjectCode("test-test");
        project.setDescription("test test test test");
        project.setProgressPercentage((byte) 50);
        project.setBudget(1000);
        project.setId(1L);
        project.setCosts(100);
        project.setUpdatedOn(new Date());
        project.setCreatedOn(new Date(100));
        project.setDeadline(LocalDate.of(2022, 12, 31));

        return project;
    }

    public static AssignmentSummaryDto generateAssignmentSummaryDto() {
        AssignmentSummaryDto dto = new AssignmentSummaryDto();

        dto.setAssignmentCode("test-test-er");
        dto.setDescriptionFinishedWork("Test test test test test");
        dto.setHoursWorked((short) 100);
        dto.setId(1L);
        dto.setProgressPercentage((byte) 100);

        return dto;
    }

    public static AssignmentDto generateAssignmentDto() {
        AssignmentDto dto = new AssignmentDto();

        dto.setAssignmentCode("test-test-er");
        dto.setDescription("Test test test");
        dto.setBudget(10000);
        dto.setDescriptionFinishedWork("Test test test test test");
        dto.setProgressPercentage((byte) 100);
        dto.setHoursWorked((short) 100);
        dto.setId(1L);
        dto.setCosts(0);

        Map<Long, Integer> amountOfComponentsById = new HashMap<>();
        dto.setAmountOfComponentById(amountOfComponentsById);

        ProjectSummaryDto projectDto = new ProjectSummaryDto();
        dto.setProject(projectDto);

        AccountSummaryDto accountDto = generateAccountSummaryDto();
        dto.setAccount(accountDto);

        return dto;
    }

    public static AccountSummaryDto generateAccountSummaryDto() {
        AccountSummaryDto dto = new AccountSummaryDto();
        dto.setEmployeeFunction("Tester");
        dto.setLastName("er");
        dto.setFirstName("test");
        dto.setId(1L);
        return dto;
    }

    public static Component generateComponent() {
        Component component = new Component();

        component.setDescription("test test test");
        component.setId(1L);
        component.setPrice(1000);
        component.setStock(100);
        component.setArticleNumber("test-test-test");
        component.setFileName("test.test");
        component.setFileUrl("/test/test");
        component.setManufacturer("test company");
        component.setOrderLink("http://test.test.nl/");

        Set<Assignment> assignments = new HashSet<>();
        Assignment assignment = generateAssignment();
        assignments.add(assignment);
        component.setAssignments(assignments);

        return component;
    }

    public static Assignment generateAssignment() {
        Assignment assignment = new Assignment();

        assignment.setAssignmentCode("test-test-er");
        assignment.setDescription("Test test test");
        assignment.setBudget(10000);
        assignment.setDescriptionFinishedWork("Test test test test test");
        assignment.setProgressPercentage((byte) 100);
        assignment.setHoursWorked((short) 100);
        assignment.setId(1L);
        assignment.setCosts(0);

        Map<Long, Integer> amountOfComponentsById = new HashMap<>();
        assignment.setAmountOfComponentById(amountOfComponentsById);

        Project project = new Project();
        assignment.setProject(project);

        Account account = new Account();
        assignment.setAccount(account);

        return assignment;
    }

    public static ComponentDto generateComponentDto() {
        ComponentDto dto = new ComponentDto();
        dto.setDescription("test test test");
        dto.setId(1L);
        dto.setPrice(1000);
        dto.setStock(100);
        dto.setArticleNumber("test-test-test");
        dto.setFileName("test.test");
        dto.setFileUrl("/test/test");
        dto.setManufacturer("test company");
        dto.setOrderLink("http://test.test.nl/");

        Set<AssignmentSummaryDto> assignmentDtos = new HashSet<>();
        AssignmentSummaryDto assignmentDto = new AssignmentSummaryDto();
        assignmentDtos.add(assignmentDto);
        dto.setAssignments(assignmentDtos);

        return dto;
    }

}
