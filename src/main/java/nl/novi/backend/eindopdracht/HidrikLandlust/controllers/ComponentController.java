package nl.novi.backend.eindopdracht.HidrikLandlust.controllers;

import nl.novi.backend.eindopdracht.HidrikLandlust.dto.ComponentDto;
import nl.novi.backend.eindopdracht.HidrikLandlust.exceptions.BadRequestException;
import nl.novi.backend.eindopdracht.HidrikLandlust.services.ComponentService;
import nl.novi.backend.eindopdracht.HidrikLandlust.services.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping(value = "/components")
public class ComponentController {
    @Autowired
    ComponentService componentService;

    @Autowired
    FileStorageService storageService;

    @GetMapping(value = "")
    public ResponseEntity<List<ComponentDto>> getComponents() {
        List<ComponentDto> components = componentService.getComponentsDto();

        return ResponseEntity.ok().body(components);
    }

    @PostMapping(value = "")
    public ResponseEntity<ComponentDto> createComponent(@RequestBody ComponentDto dto) {
        ComponentDto component = componentService.createComponent(dto);

        return ResponseEntity.accepted().body(component);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<ComponentDto> getComponent(@PathVariable("id") Long id) {
        ComponentDto component = componentService.getComponentDto(id);

        return ResponseEntity.accepted().body(component);
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

    @PostMapping(value = "/file/{id}")
    public ResponseEntity<String> saveComponentFile(@PathVariable(value = "id") Long id, @RequestParam("file") MultipartFile file){

        try {
            String url = storageService.save(file);
            componentService.saveFile(id, file.getOriginalFilename(), url);
            return ResponseEntity.ok().body("Uploaded the file successfully: " + file.getOriginalFilename());
        } catch (Exception e) {
            throw new BadRequestException("Could not upload the file: " + file.getOriginalFilename() + "!");

        }
    }

    @DeleteMapping(value = "/file/{id}")
    public ResponseEntity<ComponentDto> deleteFile(@PathVariable("id") Long id) {
        String filename = componentService.getSavedFileName(id);

        try {
            storageService.delete(filename);
            return ResponseEntity.accepted().build();
        } catch (Exception e) {
            throw e;
        }
    }

    @GetMapping("/file/{id}")
    @ResponseBody
    public ResponseEntity<Resource> getFile(@PathVariable Long id) {
        String filename = componentService.getSavedFileName(id);
        Resource file = storageService.load(filename);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

}
