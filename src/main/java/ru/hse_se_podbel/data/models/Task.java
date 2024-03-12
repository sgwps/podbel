package ru.hse_se_podbel.data.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.hse_se_podbel.data.models.enums.Stage;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "tasks")
@Getter
@Setter
@NoArgsConstructor
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @NotNull
    @Size(max=300)
    private String question;

    @Size(max=2800)
    private String code;

    @NotNull
    @Column(name="right_answers_count")
    private int rightAnswersCount = 0;

    @NotNull
    @Column(name="all_answers_count")
    private int allAnswersCount = 0;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Stage stage = Stage.NOT_APPROBATED;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "tasks_to_subjects", joinColumns = @JoinColumn(name = "task_id"), inverseJoinColumns = @JoinColumn(name = "subject_id"))
    private List<Subject> subjects = new ArrayList<>();

}
