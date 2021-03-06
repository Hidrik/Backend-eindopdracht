package nl.novi.backend.eindopdracht.hidriklandlust.services;

import nl.novi.backend.eindopdracht.hidriklandlust.dto.ComponentDto;
import nl.novi.backend.eindopdracht.hidriklandlust.dto.ComponentSummaryDto;
import nl.novi.backend.eindopdracht.hidriklandlust.models.entities.Assignment;
import nl.novi.backend.eindopdracht.hidriklandlust.models.entities.Component;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public interface ComponentService {
    List<ComponentSummaryDto> getComponentsDto();

    ComponentDto getComponentDto(Long id);

    ComponentDto createComponent(ComponentDto dto);

    ComponentDto updateComponent(Long id, ComponentDto dto);

    Component getComponent(Long id);

    Component saveComponent(Component component);

    void deleteComponent(Long id);

    Component addComponentToAssignment(Assignment assignment, Long componentId, Integer amount);

    Component removeComponentFromAssignment(Assignment assignment, Long componentId, Integer amount);

    Boolean hasFile(Long id);

    String saveFile(Long id, MultipartFile file);

    Resource loadFile(Long id);

    void deleteFile(Long id);

    void saveFileInfo(Component component, String fileName, String url);

    String deleteFileInfo(Component component);

    Component toComponent(ComponentDto dto);

    List<ComponentSummaryDto> toComponentSummaryDto(List<Component> components);

    ComponentSummaryDto toComponentSummaryDto(Component component);

    ComponentDto toComponentDto(Component comp);

    List<ComponentDto> toComponentDtos(List<Component> components);
}
