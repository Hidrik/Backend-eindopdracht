package nl.novi.backend.eindopdracht.HidrikLandlust.controllers;

import nl.novi.backend.eindopdracht.HidrikLandlust.dto.ComponentDto;
import nl.novi.backend.eindopdracht.HidrikLandlust.exceptions.AlreadyExistsException;
import nl.novi.backend.eindopdracht.HidrikLandlust.exceptions.RecordNotFoundException;
import nl.novi.backend.eindopdracht.HidrikLandlust.services.ComponentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "/components")
@CrossOrigin
public class ComponentController {
    @Autowired
    ComponentService componentService;

    @GetMapping(value = "")
    public ResponseEntity<List<ComponentDto>> getComponents() {
        List<ComponentDto> components = componentService.getComponentsDto();

        return ResponseEntity.ok().body(components);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<ComponentDto> getComponent(@PathVariable("id") Long id) {
        ComponentDto component = componentService.getComponentDto(id);

        return ResponseEntity.ok().body(component);
    }

    @PostMapping(value = "")
    public ResponseEntity<ComponentDto> createComponent(@RequestBody ComponentDto dto) {
        ComponentDto component = componentService.createComponent(dto);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/components")
                .buildAndExpand(component.getId()).toUri();

        return ResponseEntity.created(location).body(component);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<ComponentDto> updateComponent(@PathVariable("id") Long id, @RequestBody ComponentDto dto) {
        ComponentDto component = componentService.updateComponent(id, dto);

        return ResponseEntity.accepted().body(component);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<ComponentDto> deleteComponent(@PathVariable("id") Long id) {
        componentService.deleteComponent(id);

        return ResponseEntity.accepted().build();
    }

    @GetMapping("/{id}/file")
    @ResponseBody
    public ResponseEntity<Resource> getComponentFile(@PathVariable Long id) {
        Resource file = componentService.loadFile(id);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }


    @PostMapping(value = "/{id}/file")
    public ResponseEntity<String> saveComponentFile(@PathVariable(value = "id") Long id, @RequestParam("file") MultipartFile file){
        if (componentService.hasFile(id)) throw new AlreadyExistsException("Component with id " + id + " already has a file.");
        componentService.saveFile(id, file);

        return ResponseEntity.ok().body("Uploaded file successfully: " + file.getOriginalFilename());
    }

    @PutMapping(value = "/{id}/file")
    public ResponseEntity<String> updateComponentFile(@PathVariable(value = "id") Long id, @RequestParam("file") MultipartFile file){
        if (!componentService.hasFile(id)) throw new RecordNotFoundException("Component with id " + id + " has no file.");
        componentService.saveFile(id, file);

        return ResponseEntity.ok().body("Uploaded and updated file successfully: " + file.getOriginalFilename());
    }

    @DeleteMapping(value = "/{id}/file")
    public ResponseEntity<String> deleteComponentFile(@PathVariable("id") Long id) {
        componentService.deleteFile(id);

        return ResponseEntity.accepted().body("Removed file from component with id " + id);
    }


}
