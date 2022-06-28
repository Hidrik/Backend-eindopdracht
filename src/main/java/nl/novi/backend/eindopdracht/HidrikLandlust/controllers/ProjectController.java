package nl.novi.backend.eindopdracht.HidrikLandlust.controllers;

import nl.novi.backend.eindopdracht.HidrikLandlust.dto.AssignmentDto;
import nl.novi.backend.eindopdracht.HidrikLandlust.dto.ProjectDto;
import nl.novi.backend.eindopdracht.HidrikLandlust.dto.UserDto;
import nl.novi.backend.eindopdracht.HidrikLandlust.services.*;
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

    @Autowired
    AssignmentService assignmentService;

    @Autowired
    UserService userService;

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

    @PostMapping(value = "", consumes = {"multipart/form-data"})
    public ResponseEntity<ProjectDto> createNewProject(ProjectDto dto) {

        Long id = projectService.createProject(dto);
        dto.setId(id);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{username}")
                .buildAndExpand(id).toUri();

        return ResponseEntity.created(location).body(dto);
    }

    @PutMapping(value = "/{projectCode}")
    public ResponseEntity<ProjectDto> changeProject(@PathVariable("projectCode") String projectCode, @RequestBody ProjectDto dto) {
        Long id = projectService.updateProject(projectCode, dto);

        return ResponseEntity.accepted().build();
    }

    @DeleteMapping(value = "/{projectCode}")
    public ResponseEntity<ProjectDto> deleteProject(@PathVariable("projectCode") String projectCode) {
        projectService.deleteProject(projectCode);
        return ResponseEntity.accepted().build();
    }

    @PostMapping(value = "/assignments/{projectCode}")
    public ResponseEntity<AssignmentDto> addAssignmentToProject(@PathVariable("projectCode") String projectCode, @RequestBody AssignmentDto dto) {
        AssignmentDto receivedDto = assignmentService.addAssignmentToProject(projectCode, dto);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/assignments/{projectCode}")
                .buildAndExpand(receivedDto.getId()).toUri();

        return ResponseEntity.created(location).body(receivedDto);
    }

    @PostMapping(value = "/users/{projectCode}")
    public ResponseEntity<ProjectDto> addAccountToProject(@PathVariable("projectCode") String projectCode, @RequestBody Long accountId) {
        ProjectDto dto = projectService.addAccountToProject(projectCode, accountId);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/users/{projectCode}")
                .buildAndExpand(dto.getId()).toUri();

        return ResponseEntity.created(location).body(dto);
    }

}
