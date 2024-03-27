package ru.hse_se_podbel.data.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.hse_se_podbel.data.models.AnswerOption;
import ru.hse_se_podbel.data.models.Task;
import ru.hse_se_podbel.data.models.Subject;
import ru.hse_se_podbel.data.models.enums.Module;
import ru.hse_se_podbel.data.models.enums.Stage;
import ru.hse_se_podbel.data.repositories.SubjectRepository;
import ru.hse_se_podbel.data.repositories.TaskRepository;
import ru.hse_se_podbel.data.validation.TaskFormValidator;
import ru.hse_se_podbel.forms.NewTaskForm;

import java.util.*;
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
    TaskFormValidator taskFormValidator;



    public Task saveNewTask(Task task) {
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
        taskFormValidator.validate(taskForm);
        List<AnswerOption> options = Arrays.stream(taskForm.getOptions()).filter(answerOption -> answerOption.getText().length() != 0).toList();
        List<Subject> subjects = Arrays.stream(taskForm.getSubjects()).filter(subjectBool -> subjectBool.getValue() == true).map(subjectPair -> subjectPair.getKey()).toList();
        Task task = Task.builder().id(taskForm.getId()).number(taskForm.isNew() ? taskRepository.count() + 1 :  taskForm.getNumber())
                .question(taskForm.getText()).code(taskForm.getCode()).subjects(subjects).stage(Stage.NOT_APPROBATED).build();
        task.setAnswerOptions(options);
        answerOptionService.deleteByTaskId(task.getId());
        Task savedTask = taskRepository.save(task);
        savedTask.setAnswerOptions(options.stream().map(answerOption -> {
            answerOption.setTask(savedTask);
            return answerOptionService.save(answerOption);
        }).collect(Collectors.toList()));
        return savedTask;
    }

    public Task findByNumber(long number) {
        return taskRepository.findByNumber(number);
    }
    public Optional<Task> findById(UUID id) {
        return taskRepository.findById(id);
    }


    public Task updateStage(Long number, Stage stage) {
        Task task = taskRepository.findByNumber(number);
        if (task.getStage().equals(Stage.NOT_APPROBATED) && ((stage.equals(Stage.IN_USE) || stage.equals(Stage.REJECTED))) ||
                (task.getStage().equals(Stage.IN_USE) && stage.equals(Stage.WITHDRAWN))) {
            task.setStage(stage);
        }
        else {
            throw new IllegalStateException("Некорректная операция");
        }
        return taskRepository.save(task);
    }

    public List<Task> getAllTasksBySubject(Subject subject) {
        List<Task> tasks;
        tasks = taskRepository.findBySubjectsId(subject.getId());
        return tasks;
    }

    public List<Task> getByStage(Stage stage) {
        return taskRepository.findByStage(stage);
    }


    public List<UUID> listOfTaskForModule(Module module) {
        return taskRepository.listOfTaskForModule(module.getValue());
    }


    public List<UUID> listOfTaskForSession(Module module, int count) {
        List<UUID> tasksForModule = listOfTaskForModule(module);
        Collections.shuffle(tasksForModule);
        return tasksForModule.subList(0, count);
    }

    public boolean checkAnswer(UUID id, List<String> answer) {
        Task task = findById(id).get();
        task.setAllAnswersCount(task.getAllAnswersCount() + 1);
        Set<String> correct = task.getAnswerOptions().stream().filter(i -> i.getIsCorrect()).map(i -> i.getText()).collect(Collectors.toSet());
        boolean result = correct.equals(new HashSet<>(answer));
        if (result) {
            task.setAllAnswersCount(task.getAllAnswersCount() + 1);
        };
        taskRepository.save(task);
        return result;
    }
}
