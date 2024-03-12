package ru.hse_se_podbel.data.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.hse_se_podbel.data.models.Subject;

import java.util.UUID;

public interface SubjectRepository extends JpaRepository<Subject, UUID> {
}
