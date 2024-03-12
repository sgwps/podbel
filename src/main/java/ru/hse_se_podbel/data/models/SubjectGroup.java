package ru.hse_se_podbel.data.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "subject_group")
@Getter
@Setter
@NoArgsConstructor
public class SubjectGroup {  // В ТЗ - Spectrum
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @NotNull
    @Size(max = 100)
    @Column(name = "name_russian")
    private String nameRussian;

    @NotNull
    @Size(max = 50)
    @Column(name = "name_english", unique = true)
    private String shortNameEnglish;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "subjects_to_groups", joinColumns = @JoinColumn(name = "group_id"), inverseJoinColumns = @JoinColumn(name = "subject_id"))
    private List<Subject> subjects = new ArrayList<>();
}
