package ru.hse_se_podbel.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import ru.hse_se_podbel.data.models.AnswerOption;
import ru.hse_se_podbel.data.models.Subject;
import ru.hse_se_podbel.data.models.Task;
import ru.hse_se_podbel.data.models.enums.Stage;
import ru.hse_se_podbel.data.service.SubjectService;
import ru.hse_se_podbel.data.service.TaskService;
import ru.hse_se_podbel.forms.NewTaskForm;
import ru.hse_se_podbel.forms.NewUserForm;
import ru.hse_se_podbel.forms.SubjectCheckboxForm;

import javax.validation.ValidationException;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/tasks")
@SessionAttributes("newTaskForm")
public class TaskController {

    @Autowired
    SubjectService subjectService;

    @Autowired
    TaskService taskService;



    @ModelAttribute("newTaskForm")
    public NewTaskForm newTaskForm() {
        List<AbstractMap.SimpleEntry<Subject, Boolean>> subjects =
                subjectService.getAll().stream().map(subject -> new SubjectCheckboxForm(subject, false)).collect(Collectors.toList());
        NewTaskForm newTaskForm = new NewTaskForm();
        SubjectCheckboxForm[] array = new SubjectCheckboxForm[subjects.size()];
        newTaskForm.setSubjects(subjects.toArray(array));
        return newTaskForm;
    }

    @GetMapping("/new")
    public String newTaskView(Model model, @ModelAttribute("newTaskForm") NewTaskForm newTaskForm) {
        model.addAttribute("newTaskForm", newTaskForm);
        return "/tasks/new";

    }

    @PostMapping("/new")
    public String newTask(Model model, @ModelAttribute("newTaskForm") NewTaskForm newTaskForm, BindingResult result, SessionStatus sessionStatus) {
        List<AnswerOption> options = Arrays.stream(newTaskForm.getOptions()).filter(answerOption -> answerOption.getText().length() != 0).collect(Collectors.toList());
        if (options.size() == 1) options.get(0).setIsCorrect(true);
        List<Subject> subjects = Arrays.stream(newTaskForm.getSubjects()).filter(subjectBoolPair -> subjectBoolPair.getValue() == true).map(subjectBoolPair -> subjectBoolPair.getKey()).collect(Collectors.toList());
        Task task = Task.builder().question(newTaskForm.getText()).code(newTaskForm.getCode()).subjects(subjects).answerOptions(options).stage(Stage.NOT_APPROBATED).build();
        try {
            task = taskService.saveNewTask(task);
            sessionStatus.setComplete();
            return "redirect:/tasks/view/" + Long.toString(task.getNumber());
        } catch (ValidationException e) {
            model.addAttribute("errors", "Ошибка");  // TODO - одному б-гу известо, в ччем именно ошибка, надо разные исключения на разное или хз как
            return newTaskView(model, newTaskForm); // TODO - отображение вариантов ответов на фронте
        }


    }


    @GetMapping("/view/{number}")
    public String viewTask(@PathVariable long number, Model model) {  // TODO - выброс 404
        Task task = taskService.findByNumber(number);
        model.addAttribute("task", task);

        return "/tasks/view";
    }

    @PatchMapping("/aprobate/{number}")
    public String aprobateTask(@PathVariable long number) {
        Task task = taskService.findByNumber(number);
        if (task.getStage().equals(Stage.NOT_APPROBATED)) {
            task.setStage(Stage.IN_USE);
        }
        taskService.updateStage(task);
        return "redirect:/tasks/view/" + Long.toString(task.getNumber());
    }

    @PatchMapping("/withdraw/{number}")
    public String withdrawTask(@PathVariable long number) {
        Task task = taskService.findByNumber(number);
        if (task.getStage().equals(Stage.IN_USE)) {
            task.setStage(Stage.WITHDRAWN);
        }
        taskService.updateStage(task);
        return "redirect:/tasks/view/" + Long.toString(task.getNumber());
    }


    @PatchMapping("/reject/{number}")
    public String rejectTask(@PathVariable long number) {
        Task task = taskService.findByNumber(number);
        if (task.getStage().equals(Stage.NOT_APPROBATED)) {
            task.setStage(Stage.REJECTED);
        }
        taskService.updateStage(task);
        return "redirect:/tasks/view/" + Long.toString(task.getNumber());
    }
}
