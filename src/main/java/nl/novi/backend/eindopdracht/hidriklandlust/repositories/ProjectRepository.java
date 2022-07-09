package nl.novi.backend.eindopdracht.hidriklandlust.repositories;

import nl.novi.backend.eindopdracht.hidriklandlust.models.entities.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProjectRepository extends JpaRepository<Project, Long> {

    boolean existsByProjectCode(String projectCode);

    Optional<Project> findByProjectCode(String code);
}
