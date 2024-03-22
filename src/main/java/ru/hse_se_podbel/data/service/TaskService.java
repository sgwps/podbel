package ru.hse_se_podbel.data.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.hse_se_podbel.configuration.ValueValidator;
import ru.hse_se_podbel.data.models.AnswerOption;
import ru.hse_se_podbel.data.models.Task;
import ru.hse_se_podbel.data.models.Subject;
import ru.hse_se_podbel.data.models.enums.Stage;
import ru.hse_se_podbel.data.repositories.TaskRepository;
import ru.hse_se_podbel.forms.NewTaskForm;

import javax.validation.ValidationException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskService {
    @Autowired
    TaskRepository taskRepository;
    @Autowired
    AnswerOptionService answerOptionService;

    @Autowired
    ValueValidator valueValidator;

    public Task saveNewTask(Task task) {
        if (!valueValidator.validateTask(task)) {
            throw new ValidationException();
        }
        task.setNumber(taskRepository.count() + 1);
        List<AnswerOption> answerOptionList = task.getAnswerOptions();
        Task savedTask = taskRepository.save(task);
        savedTask.setAnswerOptions(answerOptionList.stream().map(answerOption -> {
            answerOption.setTask(savedTask);
            return answerOptionService.save(answerOption);
        }).collect(Collectors.toList()));
        return savedTask;
    }

    public Task saveNewTask(NewTaskForm taskForm) {
        List<AnswerOption> options = Arrays.stream(taskForm.getOptions()).filter(answerOption -> answerOption.getText().length() != 0).toList();
        List<Subject> subjects = Arrays.stream(taskForm.getSubjects()).filter(subjectBool -> subjectBool.getValue() == true).map(subjectPair -> subjectPair.getKey()).toList();
        Task task = Task.builder().number(taskRepository.count() + 1).question(taskForm.getText()).code(taskForm.getCode()).subjects(subjects).answerOptions(new ArrayList<>()).stage(Stage.NOT_APPROBATED).build();
        if (!valueValidator.validateTask(task)) {
            throw new ValidationException();
        }
        taskRepository.save(task);
        task.setAnswerOptions(options.stream().map(answerOption -> {
            answerOption.setTask(task);
            return answerOptionService.save(answerOption);
        }).toList());
        return task;
    }

    public Task findByNumber(long number) {
        return taskRepository.findByNumber(number);
    }

    public Task updateStage(Long number, Stage stage) {
        Task task = taskRepository.findByNumber(number);
        task.setStage(stage);
        return taskRepository.save(task);
    }
}
