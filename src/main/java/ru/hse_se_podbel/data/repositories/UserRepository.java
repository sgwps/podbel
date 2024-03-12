package ru.hse_se_podbel.data.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.hse_se_podbel.data.models.UserModel;
import ru.hse_se_podbel.data.models.UserModel;

import java.util.UUID;

public interface UserRepository extends JpaRepository<UserModel, UUID> {
    UserModel findByUsername(String username);
}
