package ru.hse_se_podbel.data.validation;

import org.springframework.stereotype.Component;
import ru.hse_se_podbel.data.models.User;

import javax.validation.ValidationException;
import java.util.regex.Pattern;

@Component
public class UserProfileValidator extends PasswordValidator{
    private final String usernameRegexp = "^[a-z0-9_]{5,20}$";

    public boolean validateUsername(String username) {
        return Pattern.matches(usernameRegexp, username);
    }


    public User validate(User user) {
        if (!validateUserPassword(user.getPassword())) {
            throw new ValidationException("Пароль не соответсвует критериям:\nДолжен содержать от 10 до 20 символов\nМожет содержать только строчные и заглавные латинские буквы и цифры");
        }
        if (!validateUsername(user.getUsername())) {
            throw new ValidationException("Логин не соответсвует критериям:\nДолжен содержать от 5 до 20 символов\nМожет содержать только строчные и заглавные латинские буквы, цифры и символ подчеркивания");
        }
        return user;
    }
}
