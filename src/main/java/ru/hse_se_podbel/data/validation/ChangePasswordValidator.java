package ru.hse_se_podbel.data.validation;

import com.sun.jdi.connect.VMStartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.hse_se_podbel.data.models.User;
import ru.hse_se_podbel.forms.ChangePasswordForm;

import javax.validation.ValidationException;

@Component
public class ChangePasswordValidator extends PasswordValidator {



    public String validate(User user, ChangePasswordForm changePasswordForm) {
        if (!passwordEncoderConfig.getPasswordEncoder().matches(changePasswordForm.getOldPassword(), user.getPassword())) {
            throw new ValidationException("Неправильный старый пароль");
        }
        if (!changePasswordForm.getNewPassword().equals(changePasswordForm.getNewPasswordConfirmation())) {
            throw new ValidationException("Пароли не совпадают");
        }
        if (changePasswordForm.getNewPassword().equals(changePasswordForm.getOldPassword())) {
            throw new ValidationException("Новый пароль не может совпадать со старым");
        }
        if (!validateUserPassword(changePasswordForm.getNewPassword())) {
            throw new ValidationException("Пароль не соответсвует критериям:\nДолжен содержать от 10 до 20 символов\nМожет содержать только строчные и заглавные латинские буквы и цифры");
        }
        return changePasswordForm.getNewPassword();
    }

}
