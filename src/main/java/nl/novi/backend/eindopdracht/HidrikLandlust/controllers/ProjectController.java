package nl.novi.backend.eindopdracht.HidrikLandlust.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/projects")
public class ProjectController {
    @GetMapping(value = "")
    public ResponseEntity<String> getProject() {
        return ResponseEntity.ok().body("Hallo");
    }

}
