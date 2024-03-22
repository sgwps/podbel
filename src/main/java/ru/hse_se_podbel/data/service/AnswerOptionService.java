package ru.hse_se_podbel.data.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.hse_se_podbel.data.models.AnswerOption;
import ru.hse_se_podbel.data.repositories.AnswerOptionRepository;

import java.lang.reflect.Array;
import java.util.UUID;

@Service
public class AnswerOptionService {
    @Autowired
    AnswerOptionRepository answerOptionRepository;

    public AnswerOption save(AnswerOption answerOption) {
        return answerOptionRepository.save(answerOption);
    }

    public void deleteByTaskId(UUID taskId) {
        answerOptionRepository.deleteByTaskId(taskId);
    }

}
