package nl.novi.backend.eindopdracht.HidrikLandlust;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.novi.backend.eindopdracht.HidrikLandlust.dto.*;
import nl.novi.backend.eindopdracht.HidrikLandlust.models.entities.Account;
import nl.novi.backend.eindopdracht.HidrikLandlust.models.entities.Assignment;
import nl.novi.backend.eindopdracht.HidrikLandlust.models.entities.Authority;
import nl.novi.backend.eindopdracht.HidrikLandlust.models.entities.Project;
import org.checkerframework.checker.units.qual.A;

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

    public static AccountSummaryDto generateAccountSummaryDto() {
        AccountSummaryDto dto = new AccountSummaryDto();
        dto.setEmployeeFunction("Tester");
        dto.setLastName("er");
        dto.setFirstName("test");
        dto.setId(1L);
        return dto;
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

        Set<Project> projects = new HashSet<>();
        dto.setProjects(projects);

        Set<Assignment> assignments = new HashSet<>();
        dto.setAssignments(assignments);

        return dto;
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

        Map<Long, Integer> amountOfComponentsById = new HashMap<>();
        assignment.setAmountOfComponentById(amountOfComponentsById);

        Project project = new Project();
        assignment.setProject(project);

        Account account = new Account();
        assignment.setAccount(account);

        return assignment;
    }

    public static AssignmentSummaryDto generateAssignmentSummaryDto() {
        AssignmentSummaryDto dto = new AssignmentSummaryDto();

        dto.setAssignmentCode("test-test-er");
        dto.setDescriptionFinishedWork("Test test test test test");
        dto.setHoursWorked((short) 100);
        dto.setId(1L);
        dto.setProgressPercentage((byte) 50);

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

        Map<Long, Integer> amountOfComponentsById = new HashMap<>();
        dto.setAmountOfComponentById(amountOfComponentsById);

        ProjectSummaryDto projectDto = new ProjectSummaryDto();
        dto.setProject(projectDto);

        AccountSummaryDto accountDto = generateAccountSummaryDto();
        dto.setAccount(accountDto);

        return dto;
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
        dto.setAssignments(assignmentDtos);

        return dto;
    }

}
