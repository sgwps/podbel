package ru.hse_se_podbel.data.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.hse_se_podbel.configuration.ValueValidator;
import ru.hse_se_podbel.data.models.AnswerOption;
import ru.hse_se_podbel.data.models.Task;
import ru.hse_se_podbel.data.repositories.TaskRepository;

import javax.validation.ValidationException;
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

    public Task findByNumber(long number) {
        return taskRepository.findByNumber(number);
    }

    public Task updateStage(Task task) {
        taskRepository.save(task);
        return task;
    }
}
