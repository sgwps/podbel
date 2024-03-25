package ru.hse_se_podbel.configuration;

import org.springframework.context.annotation.Configuration;
import ru.hse_se_podbel.data.models.AnswerOption;
import ru.hse_se_podbel.data.models.Task;

import java.util.regex.Pattern;

@Configuration
public class ValueValidator {
    private final String usernameRegexp = "^[a-z0-9_]{5,20}$";
    private final String passwordRegexp = "^[a-z0-9]{2,20}$"; // TODO: change for 10

    public boolean validateUserPassword(String password) {
        return Pattern.matches(passwordRegexp, password);
    }

    public boolean validateUsername(String username) {
        return Pattern.matches(usernameRegexp, username);
    }


    public boolean validateAnswerText(String text) {
        return text.length() > 0 && text.length() <= 300;
    }

    public boolean validateCode(String code) {
        String[] splitted = code.split("\n");
        if (splitted.length > 100) {
            return false;
        }
        for (String line : splitted) {
            if (line.length() > 700) {
                return false;
            }
        }
        return true;
    }


    public boolean validateTask(Task task) {
        if (!validateCode(task.getCode())) return false;
//        if (task.getAnswerOptions().size() == 1) {
//            if (!validateAnswerText(task.getAnswerOptions().get(0).getText())) return false;
//        }
        for (AnswerOption option : task.getAnswerOptions()) {
            if (!validateAnswerText(option.getText())) {
                return false;
            }
        }
        //else if (task.getAnswerOptions().size() < 4 || task.getAnswerOptions().size() > 6) return false; ???
        if (task.getSubjects().size() == 0) return false;
        return true;
    }

}
