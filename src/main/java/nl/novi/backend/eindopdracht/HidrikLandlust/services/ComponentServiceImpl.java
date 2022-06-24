package nl.novi.backend.eindopdracht.HidrikLandlust.services;

import nl.novi.backend.eindopdracht.HidrikLandlust.dto.ComponentDto;
import nl.novi.backend.eindopdracht.HidrikLandlust.exceptions.RecordNotFoundException;
import nl.novi.backend.eindopdracht.HidrikLandlust.models.entities.Assignment;
import nl.novi.backend.eindopdracht.HidrikLandlust.models.entities.Component;
import nl.novi.backend.eindopdracht.HidrikLandlust.repositories.ComponentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ComponentServiceImpl implements ComponentService {
    @Autowired
    ComponentRepository componentRepository;

    @Override
    public List<ComponentDto> getComponentsDto() {
        List<Component> components = componentRepository.findAll();
        return fromComponents(components);
    }

    @Override
    public ComponentDto getComponentDto(Long id) {
        Component component = retreiveComponent(id);
        return fromComponent(component);
    }

    @Override
    public ComponentDto createComponent(ComponentDto dto) {
        Component component = toComponent(dto);
        Component savedComponent = componentRepository.save(component);
        return fromComponent(savedComponent);
    }

    @Override
    public ComponentDto updateComponent(Long id, ComponentDto dto) {
        Component comp = retreiveComponent(id);

 /*       if (!(dto.getFileType() == null))       comp.setFileType(dto.getFileType());
        if (!(dto.getFileName() == null))       comp.setFileName(dto.getFileName());
        if (!(dto.getFileData() == null))       comp.setFileData(dto.getFileData());*/

        if (!(dto.getOrderLink() == null))      comp.setOrderLink(dto.getOrderLink());
        if (!(dto.getArticleNumber() == null))  comp.setArticleNumber(dto.getArticleNumber());
        if (!(dto.getPrice() == null))          comp.setPrice(dto.getPrice());
        if (!(dto.getId() == null))             comp.setId(dto.getId());
        if (!(dto.getStock() == null))          comp.setStock(dto.getStock());
        if (!(dto.getManufacturer() == null))   comp.setManufacturer(dto.getManufacturer());
        if (!(dto.getDescription() == null))    comp.setDescription(dto.getDescription());

        Component savedComponent = componentRepository.save(comp);

        return fromComponent(savedComponent);
    }

    @Override
    public Component retreiveComponent(Long id) {
        Optional<Component> optionalComponent = componentRepository.findById(id);

        if (optionalComponent.isPresent()) {
            return optionalComponent.get();
        } else {
            throw new RecordNotFoundException("Cant find component with id " + id);
        }
    }

    @Override
    public void deleteComponent(Long id) {
        componentRepository.delete(retreiveComponent(id));
    }

    @Override
    public String getSavedFileName(Long id) {
        Component component = retreiveComponent(id);
        //MultipartFile file = component.getFileData();

        return component.getFileName();
    }

    @Override
    public ComponentDto saveFile(Long id, String fileName, String url) {
        Component component = retreiveComponent(id);
        component.setFileName(fileName);
        component.setFileUrl(url);

        Component savedComponent = componentRepository.save(component);

        return fromComponent(savedComponent);
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


        for (Assignment ass : dto.getAssignments()) {
            comp.addAssignment(ass);
        }
        return comp;
    }

    @Override
    public ComponentDto fromComponent(Component comp) {

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
        dto.setAssignments(comp.getAssignments());

        return dto;
    }

    @Override
    public List<ComponentDto> fromComponents(List<Component> components) {
        List<ComponentDto> dtos = new ArrayList<>();
        for (Component comp: components) {
            dtos.add(fromComponent(comp));
        }
        return dtos;
    }
}
