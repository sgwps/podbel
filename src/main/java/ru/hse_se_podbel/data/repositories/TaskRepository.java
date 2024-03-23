package ru.hse_se_podbel.data.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.hse_se_podbel.data.models.Task;
import ru.hse_se_podbel.data.models.enums.Stage;

import java.util.List;
import java.util.UUID;

public interface TaskRepository extends JpaRepository<Task, UUID> {
    Task findByNumber(long number);
    List<Task> findBySubjects_Id(UUID id);

    List<Task> findByStage(Stage stage);
}
