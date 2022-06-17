package nl.novi.backend.eindopdracht.HidrikLandlust.controllers;

import nl.novi.backend.eindopdracht.HidrikLandlust.dto.ProjectDto;
import nl.novi.backend.eindopdracht.HidrikLandlust.services.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "/projects")
public class ProjectController {

    @Autowired
    ProjectService projectService;

    @GetMapping(value = "")
    public ResponseEntity<List<ProjectDto>> getAllProjects() {
        List<ProjectDto> projectDtoList = projectService.getProjects();
        return ResponseEntity.ok().body(projectDtoList);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<ProjectDto> getOneProject(@PathVariable("id") Long id) {
        ProjectDto optionalProject = projectService.getProject(id);
        return ResponseEntity.ok().body(optionalProject);
    }

    @PostMapping(value = "")
    public ResponseEntity<ProjectDto> createNewProject(@RequestBody ProjectDto dto) {

        Long id = projectService.createProject(dto);
        dto.setId(id);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{username}")
                .buildAndExpand(id).toUri();

        return ResponseEntity.created(location).body(dto);
    }

}
