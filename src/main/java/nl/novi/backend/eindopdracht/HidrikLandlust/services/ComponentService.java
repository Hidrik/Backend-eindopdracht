package nl.novi.backend.eindopdracht.HidrikLandlust.services;

import nl.novi.backend.eindopdracht.HidrikLandlust.dto.AssignmentDto;
import nl.novi.backend.eindopdracht.HidrikLandlust.dto.ComponentDto;
import nl.novi.backend.eindopdracht.HidrikLandlust.dto.ComponentSummaryDto;
import nl.novi.backend.eindopdracht.HidrikLandlust.models.entities.Assignment;
import nl.novi.backend.eindopdracht.HidrikLandlust.models.entities.Component;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public interface ComponentService {
    List<ComponentDto> getComponentsDto();

    ComponentDto getComponentDto(Long id);

    ComponentDto createComponent(ComponentDto dto);

    ComponentDto updateComponent(Long id, ComponentDto dto);

    Component getComponent(Long id);

    boolean deleteComponent(Long id);

    Component addComponentToAssignment(Assignment assignment, Long componentId, Integer amount);

    Component removeComponentFromAssignment(Assignment assignment, Long componentId, Integer amount);

    Boolean hasFile(Long id);

    boolean saveFile(Long id, MultipartFile file);

    Resource loadFile(Long id);

    boolean deleteFile(Long id);

    boolean saveFileInfo(Long id, String fileName, String url);

    String deleteFileInfo(Long id);

    Component toComponent(ComponentDto dto);

    ComponentSummaryDto toComponentSummaryDto(Component component);

    ComponentDto toComponentDto(Component comp);

    List<ComponentDto> toComponentDtos(List<Component> components);
}
