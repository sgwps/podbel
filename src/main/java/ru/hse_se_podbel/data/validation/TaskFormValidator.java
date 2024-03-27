package ru.hse_se_podbel.data.validation;

import org.springframework.stereotype.Component;
import ru.hse_se_podbel.forms.NewTaskForm;

import javax.validation.ValidationException;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class TaskFormValidator {
    public NewTaskForm validate(NewTaskForm newTaskForm) {
        int sumLength = newTaskForm.getText().length() + newTaskForm.getCode().length();
        if (newTaskForm.getOptions().length == 0) {
            throw new ValidationException("Введите ответ на задание");

        }
        if (Arrays.stream(newTaskForm.getOptions()).anyMatch(i -> i.getText().length() == 0)) {
            throw new ValidationException("Удалите пустые варианты ответа");
        }

        if (newTaskForm.getOptions().length != 1) {
            if (newTaskForm.getOptions().length < 3 || newTaskForm.getOptions().length > 10) {
                throw new ValidationException("Количество вариантов ответа должно быть не меньше 10 и не больше 3");
            }
            int maxLength = Arrays.stream(newTaskForm.getOptions()).mapToInt(i -> i.getText().length()).max().getAsInt();
            if (maxLength > 95) {
                sumLength +=  Arrays.stream(newTaskForm.getOptions()).mapToInt(i -> i.getText().length()).sum();
            }
        }
        else {
            if (newTaskForm.getOptions()[0].getText().length() > 150) {
                throw new ValidationException("Длина ответа не может быть дольше 150 символов");
            }
        }
        if (sumLength > 4050) {
            throw new ValidationException("Сократите количество сиволов");
        }
        AtomicInteger subjectsCount = new AtomicInteger();

        Arrays.stream(newTaskForm.getSubjects()).forEach(i -> {
            if (i.getValue()) subjectsCount.getAndIncrement();
        });
        if (subjectsCount.get() <= 0)  throw new ValidationException("Выберите хотя бы одну тему");
        if (subjectsCount.get() > 5) throw new ValidationException("Задание может иметь не более 5 тем");

        return newTaskForm;
    }
}
