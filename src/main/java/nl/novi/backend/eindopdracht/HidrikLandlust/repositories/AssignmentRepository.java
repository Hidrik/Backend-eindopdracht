package nl.novi.backend.eindopdracht.HidrikLandlust.repositories;

import nl.novi.backend.eindopdracht.HidrikLandlust.models.entities.Assignment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AssignmentRepository extends JpaRepository<Assignment, Long> {
    boolean existsByAssignmentCode(String assignmentCode);
}
