package nl.novi.backend.eindopdracht.HidrikLandlust.controllers;

import nl.novi.backend.eindopdracht.HidrikLandlust.dto.AssignmentDto;
import nl.novi.backend.eindopdracht.HidrikLandlust.dto.AssignmentSummaryDto;
import nl.novi.backend.eindopdracht.HidrikLandlust.exceptions.BadRequestException;
import nl.novi.backend.eindopdracht.HidrikLandlust.services.AssignmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping(value = "/assignments")
public class AssignmentController {
    @Autowired
    AssignmentService assignmentService;

    @GetMapping(value = "")
    public ResponseEntity<List<AssignmentDto>> getAssignments() {
        List<AssignmentDto> assignments = assignmentService.getAllAssignmentsDto();

        return ResponseEntity.ok().body(assignments);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<AssignmentDto> getAssignment(@PathVariable("id") Long id) {
        AssignmentDto dto = assignmentService.getAssignmentDto(id);

        return ResponseEntity.ok().body(dto);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<AssignmentDto> updateAssignment(@PathVariable("id") Long id, @RequestBody AssignmentDto dto) {
        assignmentService.updateAssignment(id, dto);

        return ResponseEntity.ok().build();
    }

    @PutMapping(value = "/finishedWork/{id}")
    public ResponseEntity<AssignmentDto> updateFinishedWork(@PathVariable("id") Long id, @RequestBody AssignmentSummaryDto summaryDto) {
        assignmentService.updateAssignmentFinishedWork(id, summaryDto);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Object> deleteAssignment(@PathVariable("id") Long id) {
        assignmentService.deleteAssignment(id);

        return ResponseEntity.ok().build();
    }

    @PutMapping(value = "/{id}/components/{componentId}")
    public ResponseEntity<Object> addComponent(@PathVariable("id") Long assignmentId, @PathVariable("componentId") Long componentId, @RequestBody Map<String, Object> fields) {
        Integer amount;
        try {
            amount = (Integer) fields.get("amount");
        } catch (Exception e) {
            throw new BadRequestException("No amount is given, request body is faulty.");
        }

        assignmentService.addComponentToAssignment(amount, assignmentId, componentId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(value = "/{id}/components/{componentId}")
    public ResponseEntity<Object> removeComponent(@PathVariable("id") Long assignmentId, @PathVariable("componentId") Long componentId, @RequestBody Map<String, Object> fields) {
        Integer amount;
        try {
            amount = (Integer) fields.get("amount");
        } catch (Exception e) {
            throw new BadRequestException("No amount is given, request body is faulty.");
        }

        assignmentService.removeComponentFromAssignment(amount, assignmentId, componentId);

        return ResponseEntity.accepted().build();
    }

    @PutMapping(value = "/{id}/accounts/{accountId}")
    public ResponseEntity<Object> addAccountToAssignment(@PathVariable("id") Long assignmentId, @PathVariable("accountId") Long accountId) {
        assignmentService.addAccountToAssignment(assignmentId, accountId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(value = "/{id}/accounts/{accountId}")
    public ResponseEntity<Object> deleteAccountFromAssignment(@PathVariable("id") Long assignmentId, @PathVariable("accountId") Long accountId) {
        assignmentService.removeAccountFromAssignment(assignmentId, accountId);
        return ResponseEntity.ok().build();
    }

}


