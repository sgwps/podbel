package ru.hse_se_podbel.controllers;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.hse_se_podbel.data.models.Subject;
import ru.hse_se_podbel.data.models.Task;
import ru.hse_se_podbel.data.models.enums.Module;
import ru.hse_se_podbel.data.service.SubjectService;
import ru.hse_se_podbel.data.service.TaskService;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/subjects")
public class SubjectConroller {
    @Autowired
    SubjectService subjectService;
    @Autowired
    TaskService taskService;

    @GetMapping("/list")
    public String listSubjects(Model model) {
        List<Module> modules = Arrays.asList(Module.FIRST, Module.SECOND, Module.THIRD);
        List<Subject> subjects = subjectService.getAll();
        model.addAttribute("subjects", subjects);
        model.addAttribute("modules", modules);
        return "/subjects/list";
    }

    @GetMapping("/{id}")
    public String viewSubject(Model model, @PathVariable int id) {
        try {
            Subject subject = subjectService.getById(id);
            model.addAttribute("subject", subject);
            List<Task> tasks = taskService.getAllTasksBySubject(subject);
            model.addAttribute("tasks", tasks);

        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.toString());
            return "/subjects/view";
        }
        return "/subjects/view";
    }

}
