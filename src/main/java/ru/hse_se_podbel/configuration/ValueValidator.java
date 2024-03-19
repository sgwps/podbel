package ru.hse_se_podbel.configuration;

import org.springframework.context.annotation.Configuration;

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
}
