package ru.hse_se_podbel.data.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.hse_se_podbel.data.models.SubjectGroup;

import java.util.UUID;

public interface SubjectGroupRepository extends JpaRepository<SubjectGroup, UUID> {
}
