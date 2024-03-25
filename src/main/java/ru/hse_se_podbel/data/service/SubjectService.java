package ru.hse_se_podbel.data.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.hse_se_podbel.configuration.ValueValidator;
import ru.hse_se_podbel.data.models.Subject;
import ru.hse_se_podbel.data.repositories.SubjectRepository;

import javax.validation.ValidationException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class SubjectService {
    @Autowired
    SubjectRepository subjectRepository;


    public Subject save(Subject subject) {
        return subjectRepository.save(subject);
    }

    public List<Subject> getAll() {
        return subjectRepository.findAll();
    }

    public Subject getById(int id) {
        Optional<Subject> optionalSubject = subjectRepository.findById(id);
        if (optionalSubject.isPresent()) {
            return optionalSubject.get();
        } else {
            throw new EntityNotFoundException("No such subject");
        }
    }

}
