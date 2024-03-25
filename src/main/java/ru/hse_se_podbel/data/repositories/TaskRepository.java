package ru.hse_se_podbel.data.repositories;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.hse_se_podbel.data.models.Task;
import ru.hse_se_podbel.data.models.enums.Stage;

import java.util.List;
import java.util.UUID;

public interface TaskRepository extends JpaRepository<Task, UUID> {
    Task findByNumber(long number);
    List<Task> findBySubjectsId(int id);


    List<Task> findByStage(Stage stage);

    @Query(value = "select t.id as task from tasks t inner join  tasks_to_subjects ts on ts.task_id = t.id inner join subjects s on ts.subject_id = s.id group by task having max(s.module) <= :module", nativeQuery = true)
    List<UUID> listOfTaskForModule(@Param("module") int module);
}

