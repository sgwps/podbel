package ru.hse_se_podbel.configuration;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import ru.hse_se_podbel.data.models.AnswerOption;
import ru.hse_se_podbel.data.models.Subject;
import ru.hse_se_podbel.data.models.Task;
import ru.hse_se_podbel.data.models.User;
import ru.hse_se_podbel.data.models.enums.Module;
import ru.hse_se_podbel.data.models.enums.Role;
import ru.hse_se_podbel.data.models.enums.Stage;
import ru.hse_se_podbel.data.service.SubjectService;
import ru.hse_se_podbel.data.service.TaskService;
import ru.hse_se_podbel.data.service.UserService;

import javax.swing.text.html.Option;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class CommandLineAppStartupRunner implements CommandLineRunner {
    @Autowired
    UserService userService;

    @Autowired
    SubjectService subjectService;

    @Autowired
    TaskService taskService;

    private FirstUserParams firstUserParams = new FirstUserParams();

    public void createFirstUser() {
        User user = User.builder().username(firstUserParams.getUsername()).password(firstUserParams.getPassword()).role(Role.ADMIN_NOT_ACTIVATED).build();
        userService.saveNew(user);
    }

    @Value("classpath:subjects.csv")
    Resource subjectsFile;

    @Value("classpath:tasks.json")
    Resource tasksFile;

    public void saveSubjects() throws IOException {
        try (Scanner scanner = new Scanner(subjectsFile.getInputStream())) {
            while (scanner.hasNextLine()) {
                String[] line = scanner.nextLine().strip().split(";");
                Module module = Module.find(Integer.parseInt(line[2]));
                int number = Integer.parseInt(line[0]);
                Subject subject = Subject.builder().nameRussian(line[1]).id(number).build();
                subject.setModule(module);
                subjectService.save(subject);
            }
        }
    }


    public AnswerOption createOption(String correct, String option) {
        String text = option.substring(3, 4).toUpperCase() + option.substring(4);
        boolean isCorrect = correct.indexOf(option.charAt(0)) != -1;
        return AnswerOption.builder().text(text).isCorrect(isCorrect).build();
    }


    public void saveOneTask(String id, JsonNode jsonNode) {
        List<String> subjectsIdsStr = Arrays.stream(id.split("\\.")).toList();
        List<Integer> subjectsIds = subjectsIdsStr.subList(1, subjectsIdsStr.size() - 1).stream().map(i -> Integer.parseInt(i)).collect(Collectors.toList());
        List<Subject> subjects = subjectsIds.stream().map(i -> subjectService.getById(i)).collect(Collectors.toList());
        String question = jsonNode.get("question").asText();
        String code = jsonNode.get("code").asText();
        List<String> optionsRaw = new ArrayList<>();
        jsonNode.get("options").elements().forEachRemaining(element -> optionsRaw.add(element.asText()));
        String answer = jsonNode.get("answer").asText();
        List<AnswerOption> options = new ArrayList<>();
        if (!optionsRaw.isEmpty()) {
            options = optionsRaw.stream().map(option -> createOption(answer, option)).collect(Collectors.toList());
        }
        else {
            options.add(AnswerOption.builder().text(answer).isCorrect(true).build());
        }
        Task task = Task.builder().subjects(subjects).question(question).code(code).answerOptions(options).stage(Stage.IN_USE).build();
        taskService.saveNewTask(task);

    }

    public void saveTasks() throws IOException {
        try (Scanner scanner = new Scanner(tasksFile.getInputStream())) {
            String jsonString = scanner.useDelimiter("\\Z").next();
            ObjectMapper mapper = new ObjectMapper();
            JsonFactory factory = mapper.getFactory();
            JsonParser parser = factory.createParser(jsonString);
            JsonNode jsonNode = mapper.readTree(parser);
            Iterator<String> iterator = jsonNode.fieldNames();
            iterator.forEachRemaining(key -> {
                saveOneTask(key, jsonNode.get(key));
            });
        }
    }





    @Override
    public void run(String... args) throws Exception {
        createFirstUser();
        saveSubjects();
        saveTasks();
    }
}