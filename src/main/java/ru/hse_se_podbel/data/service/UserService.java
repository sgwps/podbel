package ru.hse_se_podbel.data.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.hse_se_podbel.configuration.PasswordEncoderConfig;
import ru.hse_se_podbel.configuration.ValueValidator;
import ru.hse_se_podbel.data.models.User;
import ru.hse_se_podbel.data.repositories.UserRepository;

import javax.validation.ValidationException;
import java.util.List;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    ValueValidator valueValidator;

    @Autowired
    PasswordEncoderConfig passwordEncoderConfig;

    public User save(User user, boolean encode_password) {
        if (!valueValidator.validateUserPassword(user.getPassword()) || !valueValidator.validateUsername(user.getUsername())) {
            throw new ValidationException();
        }
        if (encode_password) {
            user.setPassword(passwordEncoderConfig.getPasswordEncoder().encode(user.getPassword()));
        }
        return userRepository.save(user);
    }

    public User save(User user){
        return save(user, true);
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

    public boolean delete(String username) {
        return userRepository.deleteByUsername(username);
    }
}
