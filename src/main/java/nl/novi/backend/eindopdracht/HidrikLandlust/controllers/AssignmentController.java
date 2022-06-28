package nl.novi.backend.eindopdracht.HidrikLandlust.controllers;

import nl.novi.backend.eindopdracht.HidrikLandlust.dto.AssignmentDto;
import nl.novi.backend.eindopdracht.HidrikLandlust.services.AssignmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/assignments")
public class AssignmentController {
    @Autowired
    AssignmentService assignmentService;

    @GetMapping(value = "")
    public ResponseEntity<List<AssignmentDto>> getAssignments() {
        List<AssignmentDto> assignments = assignmentService.getAllAssignments();

        return ResponseEntity.ok().body(assignments);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<AssignmentDto> getAssignment(@PathVariable("id") Long id) {
        AssignmentDto dto = assignmentService.getAssignmentDto(id);

        return ResponseEntity.accepted().body(dto);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Object> deleteAssignment(@PathVariable("id") Long id) {
        assignmentService.removeAssignmentFromProject(id);

        return ResponseEntity.accepted().build();
    }

    @PutMapping(value = "/{id}/{componentId}") //TODO
    public ResponseEntity<Object> addComponent(@PathVariable("id") Long assignmentId, @PathVariable("componentId") Long componentId, @RequestBody Integer amount) {
        assignmentService.addComponentToAssignment(amount, assignmentId, componentId);
        return ResponseEntity.accepted().build();
    }

    @DeleteMapping(value = "/{id}/{componentId}") //TODO
    public ResponseEntity<Object> removeComponent(@PathVariable("id") Long assignmentId, @PathVariable("componentId") Long componentId, @RequestBody Integer amount) {
        assignmentService.removeComponentFromAssignment(amount, assignmentId, componentId);
        return ResponseEntity.accepted().build();
    }
}


