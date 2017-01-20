package io.swagger.database.dao;

import io.swagger.database.model.CompleteQuestion;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by lux on 14.01.17.
 */
public interface QuestionReporitory extends CrudRepository<CompleteQuestion, Long> {
}
