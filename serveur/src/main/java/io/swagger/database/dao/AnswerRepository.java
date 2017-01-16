package io.swagger.database.dao;

import io.swagger.database.model.CompleteAnswer;
import io.swagger.database.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by lux on 14.01.17.
 */
public interface AnswerRepository extends CrudRepository<CompleteAnswer, Long> {
    List<CompleteAnswer> getAnswerByUser(User user);
}
