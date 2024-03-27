package ru.hse_se_podbel.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.server.ResponseStatusException;
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

    @GetMapping("/new/{number}")
    public String newTaskViewExisting(Model model, @ModelAttribute("newTaskForm") NewTaskForm newTaskForm, @PathVariable long number) {
        Task task = taskService.findByNumber(number);
        if (task.getStage() == Stage.IN_USE || task.getStage() == Stage.WITHDRAWN) {
            throw new ResponseStatusException(HttpStatusCode.valueOf(409), "Задание не доступно для редактирования");
        }
        newTaskForm.fill(task);
        model.addAttribute("newTaskForm", newTaskForm);
        return "/tasks/new";

    }


    @PostMapping("/new")
    public String newTask(Model model, @ModelAttribute("newTaskForm") NewTaskForm newTaskForm, BindingResult result, SessionStatus sessionStatus ) throws Exception {
        try {
            Task task = taskService.saveNewTask(newTaskForm);
            sessionStatus.setComplete();
            return "redirect:view/" + Long.toString(task.getNumber());
        }
        catch (ValidationException e) {
            model.addAttribute("error", e.getMessage());
            return "/tasks/new";
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
        Task task = taskService.updateStage(number, Stage.IN_USE);
        return "redirect:/tasks/view/" + Long.toString(task.getNumber());
    }

    @PatchMapping("/withdraw/{number}")
    public String withdrawTask(@PathVariable long number) {
        Task task = taskService.updateStage(number, Stage.WITHDRAWN);
        return "redirect:/tasks/view/" + Long.toString(task.getNumber());
    }


    @PatchMapping("/reject/{number}")
    public String rejectTask(@PathVariable long number) {
        Task task = taskService.updateStage(number, Stage.REJECTED);
        return "redirect:/tasks/view/" + Long.toString(task.getNumber());
    }

    @GetMapping("/stage/{stage}")
    public String listByStage(Model model, @PathVariable Stage stage) {
        List<Task> tasks = taskService.getByStage(stage);
        model.addAttribute("tasks", tasks);
        model.addAttribute("stage", stage);
        return "/tasks/stage";
    }
}
