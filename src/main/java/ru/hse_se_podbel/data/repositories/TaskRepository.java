package ru.hse_se_podbel.data.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.hse_se_podbel.data.models.Task;

import java.util.UUID;

public interface TaskRepository extends JpaRepository<Task, UUID> {
}
