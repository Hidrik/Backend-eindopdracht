package nl.novi.backend.eindopdracht.hidriklandlust.repositories;

import nl.novi.backend.eindopdracht.hidriklandlust.models.entities.Assignment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AssignmentRepository extends JpaRepository<Assignment, Long> {
    boolean existsByAssignmentCode(String assignmentCode);
}
