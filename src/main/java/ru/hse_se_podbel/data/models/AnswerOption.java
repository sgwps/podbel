package ru.hse_se_podbel.data.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.UUID;

@Entity
@Table(name = "answer_options")
@Getter
@Setter
@NoArgsConstructor
public class AnswerOption {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "task_id")
    @NotNull
    private Task task;

    @NotNull
    @Column(name = "is_correct")
    private boolean isCorrect;

    @NotNull
    @Column(name = "is_code")
    private boolean isCode;

    @NotNull
    @Size(max = 100)
    private String text;
}
