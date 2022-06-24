package nl.novi.backend.eindopdracht.HidrikLandlust.services;

import nl.novi.backend.eindopdracht.HidrikLandlust.dto.ComponentDto;
import nl.novi.backend.eindopdracht.HidrikLandlust.models.entities.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public interface ComponentService {
    List<ComponentDto> getComponentsDto();

    ComponentDto getComponentDto(Long id);

    ComponentDto createComponent(ComponentDto dto);

    ComponentDto updateComponent(Long id, ComponentDto dto);

    Component retreiveComponent(Long id);

    void deleteComponent(Long id);

    String getSavedFileName(Long id);

    ComponentDto saveFile(Long id, String fileName, String url);

    Component toComponent(ComponentDto dto);

    ComponentDto fromComponent(Component comp);

    List<ComponentDto> fromComponents(List<Component> components);
}
