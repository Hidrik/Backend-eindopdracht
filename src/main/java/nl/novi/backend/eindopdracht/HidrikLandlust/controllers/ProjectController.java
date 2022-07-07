package nl.novi.backend.eindopdracht.HidrikLandlust.controllers;

import nl.novi.backend.eindopdracht.HidrikLandlust.dto.AssignmentDto;
import nl.novi.backend.eindopdracht.HidrikLandlust.dto.ProjectDto;
import nl.novi.backend.eindopdracht.HidrikLandlust.dto.ProjectSummaryDto;
import nl.novi.backend.eindopdracht.HidrikLandlust.services.AssignmentService;
import nl.novi.backend.eindopdracht.HidrikLandlust.services.ProjectService;
import nl.novi.backend.eindopdracht.HidrikLandlust.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "/projects")
@CrossOrigin
public class ProjectController {

    @Autowired
    ProjectService projectService;

    @Autowired
    AssignmentService assignmentService;

    @Autowired
    UserService userService;

    @GetMapping(value = "")
    public ResponseEntity<List<ProjectSummaryDto>> getAllProjects() {
        List<ProjectSummaryDto> projectDtoList = projectService.getProjectsDto();
        return ResponseEntity.ok().body(projectDtoList);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<ProjectDto> getOneProject(@PathVariable("id") Long id) {
        ProjectDto optionalProject = projectService.getProjectDto(id);
        return ResponseEntity.ok().body(optionalProject);
    }

    @PostMapping(value = "", consumes = {"multipart/form-data"})
    public ResponseEntity<ProjectSummaryDto> createNewProject(ProjectDto dto) {

        ProjectSummaryDto createdDto = projectService.createProject(dto);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{username}")
                .buildAndExpand(createdDto.getId()).toUri();

        return ResponseEntity.created(location).body(createdDto);
    }

    @PutMapping(value = "/{projectCode}")
    public ResponseEntity<ProjectDto> changeProject(@PathVariable("projectCode") String projectCode, @RequestBody ProjectDto dto) {
        ProjectDto updatedDto = projectService.updateProject(projectCode, dto);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/assignments/{projectCode}")
                .buildAndExpand(updatedDto.getId()).toUri();

        return ResponseEntity.created(location).build();
    }

    @DeleteMapping(value = "/{projectCode}")
    public ResponseEntity<ProjectDto> deleteProject(@PathVariable("projectCode") String projectCode) {
        projectService.deleteProject(projectCode);
        return ResponseEntity.accepted().build();
    }

    @PutMapping(value = "/{projectCode}/assignments")
    public ResponseEntity<AssignmentDto> addAssignmentToProject(@PathVariable("projectCode") String projectCode, @RequestBody AssignmentDto dto) {
        AssignmentDto receivedDto = assignmentService.addAssignmentToProject(projectCode, dto);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/assignments/{projectCode}")
                .buildAndExpand(receivedDto.getId()).toUri();

        return ResponseEntity.created(location).body(receivedDto);
    }

    @PutMapping(value = "/{projectCode}/accounts/{accountId}")
    public ResponseEntity<ProjectDto> addAccountToProject(@PathVariable("projectCode") String projectCode, @PathVariable("accountId")  Long accountId) {
        ProjectDto dto = projectService.addAccountToProject(projectCode, accountId);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/users/{projectCode}")
                .buildAndExpand(dto.getId()).toUri();

        return ResponseEntity.created(location).body(dto);
    }

    @DeleteMapping(value = "/{projectCode}/accounts/{accountId}")
    public ResponseEntity<ProjectDto> removeAccountFromProject(@PathVariable("projectCode") String projectCode, @PathVariable("accountId")  Long accountId) {
        projectService.removeAccountFromProject(projectCode, accountId);

        return ResponseEntity.ok().build();
    }

}
