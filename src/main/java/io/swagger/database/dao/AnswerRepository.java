package io.swagger.database.dao;

import io.swagger.database.model.CompleteAnswer;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by lux on 14.01.17.
 */
public interface AnswerRepository extends CrudRepository<CompleteAnswer, Long> {
}
