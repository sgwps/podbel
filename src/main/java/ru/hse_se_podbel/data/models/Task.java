package ru.hse_se_podbel.data.models;

import jakarta.persistence.*;
import lombok.*;
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
@AllArgsConstructor
@Builder
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @NotNull
    @Column(unique = true)
    private Long number;

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
    @Column(name = "stage")
    private Stage stage;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "tasks_to_subjects", joinColumns = @JoinColumn(name = "task_id"), inverseJoinColumns = @JoinColumn(name = "subject_id"))
    private List<Subject> subjects = new ArrayList<>();

    @OneToMany(mappedBy = "task")
    private List<AnswerOption> answerOptions;

//    public void addAnswerOptions(List<AnswerOption> options) {
//        answerOptions.addAll(options);
//        options.forEach(option -> option.setTask(this));
//    }
//
//    public void removeAnswerOption(AnswerOption option) {
//        answerOptions.remove(option);
//        option.setTask(null);
//    }

}
