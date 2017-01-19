package io.swagger.service;

import io.swagger.database.dao.AnswerRepository;
import io.swagger.database.dao.UserRepository;
import io.swagger.database.model.CompleteAnswer;
import io.swagger.database.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by lux on 19.01.17.
 */
@Service
public class TransactionnalService {

    @Autowired
    AnswerRepository answerRepository;

    @Autowired
    UserRepository userRepository;

    @Transactional
    public void saveAnswer(User userDB, CompleteAnswer completeAnswerDB)
    {
        //answerRepository.save(completeAnswerDB);
        userRepository.save(userDB);
    }
}
