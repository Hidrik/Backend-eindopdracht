package nl.novi.backend.eindopdracht.hidriklandlust.services;

import nl.novi.backend.eindopdracht.hidriklandlust.dto.AssignmentSummaryDto;
import nl.novi.backend.eindopdracht.hidriklandlust.dto.ComponentDto;
import nl.novi.backend.eindopdracht.hidriklandlust.dto.ComponentSummaryDto;
import nl.novi.backend.eindopdracht.hidriklandlust.exceptions.InternalFailureException;
import nl.novi.backend.eindopdracht.hidriklandlust.exceptions.RecordNotFoundException;
import nl.novi.backend.eindopdracht.hidriklandlust.models.entities.Assignment;
import nl.novi.backend.eindopdracht.hidriklandlust.models.entities.Component;
import nl.novi.backend.eindopdracht.hidriklandlust.repositories.ComponentRepository;
import nl.novi.backend.eindopdracht.hidriklandlust.utils.FileStorage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.net.MalformedURLException;
import java.util.Optional;

import static nl.novi.backend.eindopdracht.hidriklandlust.TestUtils.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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

    @Test
    void toComponentSummaryDtoFromComponentSucceeds() {
        Component component = generateComponent();
        ComponentSummaryDto dto = generateComponentSummaryDto();

        assertThat(componentService.toComponentSummaryDto(component)).usingRecursiveComparison().isEqualTo(dto);
    }

    @Test
    void updateComponentOnlyUpdatesPropertiesThatAreNotNullWhenAllPropertiesFromInputDtoAreNull() {
        Component component = generateComponent();
        Optional<Component> optionalComponent = Optional.of(component);
        ComponentDto inputDto = new ComponentDto();
        ComponentDto outputDto = generateComponentDto();

        when(componentRepository.findById(any(Long.class))).thenReturn(optionalComponent);
        when(componentRepository.save(any(Component.class))).thenAnswer(i -> i.getArguments()[0]);

        assertThat(componentService.updateComponent(component.getId(), inputDto))
                .usingRecursiveComparison()
                .ignoringFields("assignments")
                .isEqualTo(outputDto);
    }

    @Test
    void updateComponentUpdatesAllProperties() {
        Component component = generateComponent();
        Optional<Component> optionalComponent = Optional.of(component);
        ComponentDto dto = new ComponentDto();
        dto.setFileUrl("1");
        dto.setFileName("2");
        dto.setOrderLink("3");
        dto.setArticleNumber("4");
        dto.setPrice(10);
        dto.setId(15L);
        dto.setStock(20);
        dto.setManufacturer("5");
        dto.setDescription("6");

        when(componentRepository.findById(any(Long.class))).thenReturn(optionalComponent);
        when(componentRepository.save(any(Component.class))).thenAnswer(i -> i.getArguments()[0]);

        assertThat(componentService.updateComponent(component.getId(), dto))
                .usingRecursiveComparison()
                .ignoringFields("assignments")
                .isEqualTo(dto);
    }

    @Test
    void createComponentSetsStockAndPriceZeroWhenNoStockAndPriceIsGiven() {
        ComponentDto dto = generateComponentDto();
        dto.setStock(null);
        dto.setPrice(null);
        dto.setAssignments(null);

        when(componentRepository.save(any(Component.class))).thenAnswer(i -> i.getArguments()[0]);

        ComponentDto newDto = componentService.createComponent(dto);


        assertThat(newDto).usingRecursiveComparison().isEqualTo(dto);
        assertEquals(0, newDto.getStock());
        assertEquals(0, newDto.getPrice());
    }

    @Test
    void saveFileSucceedsWhenOldFileDoesExists() {
        Component component = generateComponent();
        Optional<Component> optionalComponent = Optional.of(component);
        MultipartFile file = new MockMultipartFile("data", "filename.txt", "text/plain", "some xml".getBytes());

        when(componentRepository.findById(any(Long.class))).thenReturn(optionalComponent);
        when(componentRepository.save(any(Component.class))).thenAnswer(i -> i.getArguments()[0]);
        when(fileStorage.save(any(), any())).thenReturn("url");
        doNothing().when(fileStorage).delete(component.getFileName(), component.getId());

        assertThat(componentService.saveFile(component.getId(),file)).isEqualTo("url");
    }

    @Test
    void saveFileThrowsInternalFailureExceptionAfterSavingFails() {
        Component component = generateComponent();
        Optional<Component> optionalComponent = Optional.of(component);
        MultipartFile file = new MockMultipartFile("data", "filename.txt", "text/plain", "some xml".getBytes());

        when(componentRepository.findById(any(Long.class))).thenReturn(optionalComponent);
        when(componentRepository.save(any(Component.class))).thenThrow(new RuntimeException("test"));
        when(fileStorage.save(any(), any())).thenReturn("url");
        doNothing().when(fileStorage).delete(component.getFileName(), component.getId());

        assertThrows(InternalFailureException.class, () -> componentService.saveFile(component.getId(),file));
    }

    @Test
    void saveFileThrowsInternalFailureExceptionAfterDeletingFails() {
        Component component = generateComponent();
        Optional<Component> optionalComponent = Optional.of(component);
        MultipartFile file = new MockMultipartFile("data", "filename.txt", "text/plain", "some xml".getBytes());

        when(componentRepository.findById(any(Long.class))).thenReturn(optionalComponent);
        when(componentRepository.save(any(Component.class))).thenAnswer(i -> i.getArguments()[0]);
        when(fileStorage.save(any(), any())).thenReturn("url");
        doThrow(new RuntimeException("test")).when(fileStorage).delete(component.getFileName(), component.getId());

        assertThrows(InternalFailureException.class, () -> componentService.saveFile(component.getId(),file));
    }

    @Test
    void loadFileSucceeds() throws MalformedURLException {
        Component component = generateComponent();
        Optional<Component> optionalComponent = Optional.of(component);
        File file = new File("/");
        Resource resource = new UrlResource(file.toPath().toUri());

        when(componentRepository.findById(any(Long.class))).thenReturn(optionalComponent);
        when(fileStorage.load(any(), any())).thenReturn(resource);

        assertEquals(resource, componentService.loadFile(component.getId()));
    }


    @Test
    void loadFileThrowsRecordNotFoundException() {
        Component component = generateComponent();
        component.setFileName(null);
        Optional<Component> optionalComponent = Optional.of(component);

        when(componentRepository.findById(any(Long.class))).thenReturn(optionalComponent);

        assertThrows(RecordNotFoundException.class, () -> componentService.loadFile(component.getId()));
    }

    @Test
    void loadFileThrowsInternalFailureException() {
        Component component = generateComponent();
        Optional<Component> optionalComponent = Optional.of(component);

        when(componentRepository.findById(any(Long.class))).thenReturn(optionalComponent);
        when(fileStorage.load(any(), any())).thenThrow(new RuntimeException());

        assertThrows(InternalFailureException.class, () -> componentService.loadFile(component.getId()));
    }


    @Test
    void deleteFileSucceeds() throws MalformedURLException {
        Component component = generateComponent();
        Optional<Component> optionalComponent = Optional.of(component);
        File file = new File("/");
        Resource resource = new UrlResource(file.toPath().toUri());

        when(componentRepository.findById(any(Long.class))).thenReturn(optionalComponent);
        doNothing().when(fileStorage).delete(any(), any());

        assertDoesNotThrow(() -> componentService.deleteFile(component.getId()));
    }


    @Test
    void deleteFileThrowsRecordNotFoundException() {
        Component component = generateComponent();
        component.setFileName(null);
        Optional<Component> optionalComponent = Optional.of(component);

        when(componentRepository.findById(any(Long.class))).thenReturn(optionalComponent);

        assertThrows(RecordNotFoundException.class, () -> componentService.deleteFile(component.getId()));
    }

    @Test
    void deleteFileThrowsInternalFailureException() {
        Component component = generateComponent();
        Optional<Component> optionalComponent = Optional.of(component);

        when(componentRepository.findById(any(Long.class))).thenReturn(optionalComponent);
        doThrow(new RuntimeException()).when(fileStorage).delete(any(), any());

        assertThrows(InternalFailureException.class, () -> componentService.deleteFile(component.getId()));
    }

}
