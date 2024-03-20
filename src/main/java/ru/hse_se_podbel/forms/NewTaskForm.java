package ru.hse_se_podbel.forms;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.hse_se_podbel.data.models.AnswerOption;
import ru.hse_se_podbel.data.models.Subject;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
public class NewTaskForm implements Form {


    private String text;
    private String code;
    private SubjectCheckboxForm[] subjects;
    private AnswerOption[] options;

    @Override
    public boolean isValid() {
        return true;
    }
}
