package ru.hse_se_podbel.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ru.hse_se_podbel.data.models.UserModel;
import ru.hse_se_podbel.data.models.enums.Role;
import ru.hse_se_podbel.data.repositories.UserRepository;
import ru.hse_se_podbel.data.service.UserService;

@Component
public class CommandLineAppStartupRunner implements CommandLineRunner {
    @Autowired
    UserService userService;

    private FirstUserParams firstUserParams = new FirstUserParams();

    public void createFirstUser() {
        UserModel user = UserModel.builder().username(firstUserParams.getUsername()).password(firstUserParams.getPassword()).role(Role.ADMIN).isActivated(true).build();
        userService.save(user);
    }

    @Override
    public void run(String... args) throws Exception {
        createFirstUser();
    }
}