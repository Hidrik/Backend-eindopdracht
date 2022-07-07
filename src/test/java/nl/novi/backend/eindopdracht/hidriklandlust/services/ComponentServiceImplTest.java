package nl.novi.backend.eindopdracht.hidriklandlust.services;


import nl.novi.backend.eindopdracht.hidriklandlust.dto.AssignmentSummaryDto;
import nl.novi.backend.eindopdracht.hidriklandlust.dto.ComponentDto;
import nl.novi.backend.eindopdracht.hidriklandlust.models.entities.Assignment;
import nl.novi.backend.eindopdracht.hidriklandlust.models.entities.Component;
import nl.novi.backend.eindopdracht.hidriklandlust.repositories.ComponentRepository;
import nl.novi.backend.eindopdracht.hidriklandlust.utils.FileStorage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static nl.novi.backend.eindopdracht.hidriklandlust.TestUtils.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
class ComponentServiceImplTest {

    @Autowired
    ComponentService componentService;

    @MockBean
    FileStorage fileStorage;

    @MockBean
    AssignmentService assignmentService;

    @MockBean
    ComponentRepository componentRepository;

    @Test
    void toComponentFromComponentDtoSucceeds() {
        Component component = generateComponent();
        ComponentDto dto = generateComponentDto();
        dto.setAssignments(null);

        assertThat(componentService.toComponent(dto))
                .usingRecursiveComparison()
                .ignoringActualNullFields()
                .isEqualTo(component);
    }

    @Test
    void toComponentDtoFromComponentSucceeds() {
        Component component = generateComponent();
        ComponentDto dto = generateComponentDto();
        AssignmentSummaryDto assignmentDto = generateAssignmentSummaryDto();

        when(assignmentService.toAssignmentSummaryDto(any(Assignment.class))).thenReturn(assignmentDto);

        assertThat(componentService.toComponentDto(component))
                .usingRecursiveComparison()
                .ignoringActualNullFields()
                .ignoringFields("assignments")
                .isEqualTo(dto);
    }

    //TODO
}
