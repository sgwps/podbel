package ru.hse_se_podbel.data.service;


import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.hse_se_podbel.configuration.PasswordEncoderConfig;
import ru.hse_se_podbel.data.models.User;
import ru.hse_se_podbel.data.models.enums.Role;
import ru.hse_se_podbel.data.repositories.UserRepository;
import ru.hse_se_podbel.data.validation.ChangePasswordValidator;
import ru.hse_se_podbel.data.validation.UserProfileValidator;
import ru.hse_se_podbel.forms.ChangePasswordForm;

import javax.validation.ValidationException;
import java.util.List;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    UserRepository userRepository;


    @Autowired
    PasswordEncoderConfig passwordEncoderConfig;

    @Autowired
    UserProfileValidator userProfileValidator;

    @Autowired
    ChangePasswordValidator changePasswordValidator;

    public User saveNew(User user, boolean encode_password) {
        userProfileValidator.validate(user);
        if (userRepository.findByUsername(user.getUsername()) != null) {
            throw new ValidationException("Пользователем с таким логином уже существует.");
        }
        if (encode_password) {
            user.setPassword(passwordEncoderConfig.getPasswordEncoder().encode(user.getPassword()));
        }

        return userRepository.save(user);
    }

    public User saveNew(User user){
        return saveNew(user, true);
    }


    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return user;

    }

    public List<User> loadAll() {
        return userRepository.findAll();
    }

    @Transactional
    public int delete(String username) {
        return userRepository.deleteByUsername(username);
    }

    public void activate(User user) {
        if (user.getRole() == Role.ADMIN_NOT_ACTIVATED) {
            user.setRole(Role.ADMIN);
        }
        if (user.getRole() == Role.USER_NOT_ACTIVATED) {
            user.setRole(Role.USER);        }
    }

    public User updatePassword(User user, ChangePasswordForm changePasswordForm) {
        changePasswordValidator.validate(user, changePasswordForm);
        user.setPassword(passwordEncoderConfig.getPasswordEncoder().encode(changePasswordForm.getNewPassword()));
        activate(user);
        userRepository.save(user);
        return user;
    }
}
