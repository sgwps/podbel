package ru.hse_se_podbel.data.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.hse_se_podbel.data.models.SubjectGroup;
import ru.hse_se_podbel.data.repositories.SubjectGroupRepository;

@Service
public class SubjectGroupService {
    @Autowired
    SubjectGroupRepository subjectGroupRepository;

    public SubjectGroup save(SubjectGroup subjectGroup) {
        return subjectGroupRepository.save(subjectGroup);
    }
}
