package ru.hse_se_podbel.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.hse_se_podbel.data.models.Task;
import ru.hse_se_podbel.data.models.User;
import ru.hse_se_podbel.data.models.enums.Module;
import ru.hse_se_podbel.data.other.UserAnswer;
import ru.hse_se_podbel.data.service.TaskService;

import javax.management.ObjectName;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/telegram-api")
public class TelegramController {
    @Autowired
    TaskService taskService;

    @GetMapping("/max-task-count")
    @ResponseBody
    public String getMaxTaskCount(@RequestParam("module") int module) {
        List<UUID> list = taskService.listOfTaskForModule(Module.find(module));
        ObjectMapper mapper = new ObjectMapper();
        JsonNode countNode = mapper.valueToTree(list.size());
        ObjectNode node = mapper.createObjectNode();
        node.set("count", countNode);
        return node.toString();
    }

    @GetMapping("/task-sequence")
    @ResponseBody
    public String getTasksForSession(@RequestParam("module") int module, @RequestParam("count")  int count) {
        List<UUID> list = taskService.listOfTaskForSession(Module.find(module), count);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode listNode = mapper.valueToTree(list);
        ObjectNode node = mapper.createObjectNode();
        node.set("tasks", listNode);
        return node.toString();
    }

    @GetMapping("/task")
    @ResponseBody
    public String getTask(@RequestParam("id") String id) {
        Task task = taskService.findById(UUID.fromString(id)).get();
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        node.set("question", mapper.valueToTree(task.getQuestion()));
        node.set("code", mapper.valueToTree(task.getCode()));
        if (task.getAnswerOptions().size() != 1) {
            Collections.shuffle(task.getAnswerOptions());
            node.set("options", mapper.valueToTree(task.getAnswerOptions().stream().map(i -> i.getText()).collect(Collectors.toList())));
        }
        return node.toString();
    }


    @PostMapping(value = "/check")
    @ResponseBody
    public String checkAnswer(@RequestBody UserAnswer answer) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        node.set("result", mapper.valueToTree(taskService.checkAnswer(UUID.fromString(answer.getTask()), answer.getOptions())));
        return node.toString();

    }

}


