package ru.hse_se_podbel.forms;

import ru.hse_se_podbel.data.models.Subject;

import java.util.AbstractMap;

public class SubjectCheckboxForm extends AbstractMap.SimpleEntry<Subject, Boolean>{
    public SubjectCheckboxForm(Subject key, Boolean value) {
        super(key, value);
    }

    public SubjectCheckboxForm() {
        super(null, false);
    }
}
