package ru.hse_se_podbel.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import ru.hse_se_podbel.data.models.Subject;
import ru.hse_se_podbel.data.models.User;
import ru.hse_se_podbel.data.models.enums.Module;
import ru.hse_se_podbel.data.models.enums.Role;
import ru.hse_se_podbel.data.service.SubjectService;
import ru.hse_se_podbel.data.service.UserService;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

@Component
public class CommandLineAppStartupRunner implements CommandLineRunner {
    @Autowired
    UserService userService;

    @Autowired
    SubjectService subjectService;

    private FirstUserParams firstUserParams = new FirstUserParams();

    public void createFirstUser() {
        User user = User.builder().username(firstUserParams.getUsername()).password(firstUserParams.getPassword()).role(Role.ADMIN).build();  // TODO: вернуть неактивированный
        userService.save(user);
    }

    @Value("classpath:subjects.csv")
    Resource resourceFile;

    public void saveSubjects() throws IOException {
        int i = 0;
        try (Scanner scanner = new Scanner(resourceFile.getFile())) {
            while (scanner.hasNextLine()) {
                String[] line = scanner.nextLine().strip().split(";");
                Module module = Module.values()[Integer.parseInt(line[1]) - 1];
                Subject subject = Subject.builder().nameRussian(line[0]).module(module).shortNameEnglish(Integer.toString(i)).build();
                subjectService.save(subject);
                i++;

            }
        }
    }

    @Override
    public void run(String... args) throws Exception {
        createFirstUser();
        saveSubjects();
    }
}