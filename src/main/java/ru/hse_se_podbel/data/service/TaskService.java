package ru.hse_se_podbel.data.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.hse_se_podbel.configuration.ValueValidator;
import ru.hse_se_podbel.data.models.AnswerOption;
import ru.hse_se_podbel.data.models.Task;
import ru.hse_se_podbel.data.models.Subject;
import ru.hse_se_podbel.data.models.enums.Stage;
import ru.hse_se_podbel.data.repositories.SubjectRepository;
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
    SubjectRepository subjectRepository;

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



    @Transactional
    public Task saveNewTask(NewTaskForm taskForm) {
        List<AnswerOption> options = Arrays.stream(taskForm.getOptions()).filter(answerOption -> answerOption.getText().length() != 0).toList();
        List<Subject> subjects = Arrays.stream(taskForm.getSubjects()).filter(subjectBool -> subjectBool.getValue() == true).map(subjectPair -> subjectPair.getKey()).toList();
        Task task = Task.builder().id(taskForm.getId()).number(taskForm.isNew() ? taskRepository.count() + 1 :  taskForm.getNumber())
                .question(taskForm.getText()).code(taskForm.getCode()).subjects(subjects).stage(Stage.NOT_APPROBATED).build();
        task.setAnswerOptions(options);
        if (!valueValidator.validateTask(task)) {
            throw new ValidationException();
        }
        taskRepository.save(task);
        answerOptionService.deleteByTaskId(task.getId());
        options.stream().forEach(answerOption -> {answerOption.setTask(task); answerOptionService.save(answerOption);});

        return task;
    }

    public Task findByNumber(long number) {
        return taskRepository.findByNumber(number);
    }


    public Task updateStage(Long number, Stage stage) {
        Task task = taskRepository.findByNumber(number);
        if (task.getStage().equals(Stage.NOT_APPROBATED) && ((stage.equals(Stage.IN_USE) || stage.equals(Stage.REJECTED))) ||
                (task.getStage().equals(Stage.IN_USE) && stage.equals(Stage.WITHDRAWN))) {
            task.setStage(stage);
        }
        return taskRepository.save(task);
    }

    public List<Task> getAllTasksBySubject(Subject subject) {
        List<Task> tasks;
        tasks = taskRepository.findBySubjects_Id(subject.getId());
        return tasks;
    }

    public List<Task> getByStage(Stage stage) {
        return taskRepository.findByStage(stage);
    }

}
