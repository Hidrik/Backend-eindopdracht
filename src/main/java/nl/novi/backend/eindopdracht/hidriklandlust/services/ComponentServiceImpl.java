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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Service
public class ComponentServiceImpl implements ComponentService {
    @Autowired
    ComponentRepository componentRepository;

    @Autowired
    FileStorage fileStorage;

    //Not autowired because of infinite recursion
    private final AssignmentService assignmentService = new AssignmentServiceImpl();

    @Override
    public List<ComponentSummaryDto> getComponentsDto() {
        List<Component> components = componentRepository.findAll();
        return toComponentSummaryDto(components);
    }

    @Override
    public List<ComponentDto> toComponentDtos(List<Component> components) {
        List<ComponentDto> dtos = new ArrayList<>();
        for (Component comp : components) {
            dtos.add(toComponentDto(comp));
        }
        return dtos;
    }

    @Override
    public ComponentDto getComponentDto(Long id) {
        Component component = getComponent(id);
        return toComponentDto(component);
    }

    @Override
    public ComponentDto createComponent(ComponentDto dto) {
        //Initialize variables when not specifiek when created
        if (dto.getStock() == null) dto.setStock(0);
        if (dto.getPrice() == null) dto.setPrice(0);

        Component component = toComponent(dto);
        Component savedComponent = saveComponent(component);
        return toComponentDto(savedComponent);
    }

    @Override
    public Component toComponent(ComponentDto dto) {
        Component comp = new Component();

        comp.setFileUrl(dto.getFileUrl());
        comp.setFileName(dto.getFileName());

        comp.setOrderLink(dto.getOrderLink());
        comp.setArticleNumber(dto.getArticleNumber());
        comp.setPrice(dto.getPrice());
        comp.setId(dto.getId());
        comp.setStock(dto.getStock());
        comp.setManufacturer(dto.getManufacturer());
        comp.setDescription(dto.getDescription());

        if (dto.getAssignments() == null) return comp;

        for (AssignmentSummaryDto assignmentDto : dto.getAssignments()) {

            comp.addAssignment(assignmentService.toAssignment(assignmentDto));
        }
        return comp;
    }

    @Override
    public ComponentDto updateComponent(Long id, ComponentDto dto) {
        Component comp = getComponent(id);

        if (dto.getFileUrl() != null) comp.setFileUrl(dto.getFileUrl());
        if (dto.getFileName() != null) comp.setFileName(dto.getFileName());
        if (dto.getOrderLink() != null) comp.setOrderLink(dto.getOrderLink());
        if (dto.getArticleNumber() != null) comp.setArticleNumber(dto.getArticleNumber());
        if (dto.getPrice() != null) comp.setPrice(dto.getPrice());
        if (dto.getId() != null) comp.setId(dto.getId());
        if (dto.getStock() != null) comp.setStock(dto.getStock());
        if (dto.getManufacturer() != null) comp.setManufacturer(dto.getManufacturer());
        if (dto.getDescription() != null) comp.setDescription(dto.getDescription());

        Component savedComponent = saveComponent(comp);

        return toComponentDto(savedComponent);
    }

    @Override
    public ComponentDto toComponentDto(Component comp) {

        ComponentDto dto = new ComponentDto();

        dto.setFileUrl(comp.getFileUrl());
        dto.setFileName(comp.getFileName());

        dto.setOrderLink(comp.getOrderLink());
        dto.setArticleNumber(comp.getArticleNumber());
        dto.setPrice(comp.getPrice());
        dto.setId(comp.getId());
        dto.setStock(comp.getStock());
        dto.setManufacturer(comp.getManufacturer());
        dto.setDescription(comp.getDescription());

        Set<AssignmentSummaryDto> assignmentSummaryDtos = new HashSet<>();

        if (comp.getAssignments() == null) return dto;

        for (Assignment ass : comp.getAssignments()) {
            assignmentSummaryDtos.add(assignmentService.toAssignmentSummaryDto(ass));
        }
        dto.setAssignments(assignmentSummaryDtos);

        return dto;
    }

    @Override
    public void deleteComponent(Long id) {
        try {
            componentRepository.delete(getComponent(id));
        } catch (Exception e) {
            throw new InternalFailureException("Cant delete component");
        }

    }

    @Override
    public Component addComponentToAssignment(Assignment assignment, Long componentId, Integer amount) {
        Component component = getComponent(componentId);
        component.setStock(component.getStock() - amount);
        component.addAssignment(assignment);
        return saveComponent(component);
    }

    @Override
    public Component removeComponentFromAssignment(Assignment assignment, Long componentId, Integer amount) {
        Component component = getComponent(componentId);

        component.deleteAssignment(assignment);
        component.setStock(component.getStock() + amount);
        return saveComponent(component);
    }

    @Override
    public String saveFile(Long id, MultipartFile file) {
        String fileName = file.getOriginalFilename();
        String url;
        Component component = getComponent(id);
        String oldFileName = component.getFileName();

        //First delete old- and save new file.
        try {
            if (oldFileName != null) fileStorage.delete(oldFileName, id);
            url = fileStorage.save(file, id);
        } catch (Exception e) {
            throw new InternalFailureException("Uploading file failed, cant save file: " + fileName);
        }
        saveFileInfo(component, fileName, url);
        return url;
    }

    @Override
    public void saveFileInfo(Component component, String fileName, String url) {
        component.setFileName(fileName);
        component.setFileUrl(url);
        saveComponent(component);
    }

    @Override
    public Resource loadFile(Long id) {
        String fileName = getComponent(id).getFileName();
        if (fileName == null) throw new RecordNotFoundException("Component with id " + id + " has no file attached.");

        try {
            return fileStorage.load(fileName, id);
        } catch (Exception e) {
            throw new InternalFailureException("Cant load file " + fileName);

        }
    }

    @Override
    public void deleteFile(Long id) {
        Component component = getComponent(id);
        String fileName = component.getFileName();
        if (fileName == null) throw new RecordNotFoundException(String.format("Component %s has no file attached.", id));
        deleteFileInfo(component);
        try {
            fileStorage.delete(fileName, id);
        } catch (Exception e) {
            throw new InternalFailureException("File from component with id " + id + " can not be removed or is already removed.");
        }
    }

    @Override
    public String deleteFileInfo(Component component) {
        String fileName = component.getFileName();
        component.setFileName(null);
        component.setFileUrl(null);
        saveComponent(component);
        return fileName;
    }

    @Override
    public Component saveComponent(Component component) {
        try {
            return componentRepository.save(component);
        } catch (Exception e) {
            throw new InternalFailureException("Can not save component to database.");
        }
    }

    @Override
    public Boolean hasFile(Long id) {
        Component component = getComponent(id);
        return component.getFileName() != null;
    }

    @Override
    public Component getComponent(Long id) {
        Optional<Component> optionalComponent = componentRepository.findById(id);

        if (optionalComponent.isPresent()) {
            return optionalComponent.get();
        } else {
            throw new RecordNotFoundException("Cant find component with id " + id);
        }
    }

    @Override
    public List<ComponentSummaryDto> toComponentSummaryDto(List<Component> components) {
        List<ComponentSummaryDto> dtos = new ArrayList<>();
        for (Component component : components) {
            dtos.add(toComponentSummaryDto(component));
        }
        return dtos;
    }

    @Override
    public ComponentSummaryDto toComponentSummaryDto(Component component) {
        ComponentSummaryDto dto = new ComponentSummaryDto();
        dto.setArticleNumber(component.getArticleNumber());
        dto.setDescription(component.getDescription());
        dto.setId(component.getId());
        dto.setPrice(component.getPrice());
        dto.setStock(component.getStock());
        dto.setManufacturer(component.getManufacturer());
        dto.setOrderLink(component.getOrderLink());
        return dto;
    }

}
