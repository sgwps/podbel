package ru.hse_se_podbel.data.models;

import jakarta.persistence.*;
import lombok.*;
import ru.hse_se_podbel.data.models.enums.Module;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "subjects")
@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class Subject { // В ТЗ - GROUP
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @NotNull
    @Size(max = 100)
    @Column(name = "name_russian")
    private String nameRussian;

    @NotNull
    @Size(max = 50)
    @Column(name = "name_english", unique = true)
    private String shortNameEnglish;



    @Column(name = "module")
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private int module;

    public void setModule(Module value) {
        this.module = value.getValue();
    }

    public Module getModule() {
        return Module.find(module);
    }


}
