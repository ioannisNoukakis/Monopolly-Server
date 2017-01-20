package io.swagger.database.dao;

import io.swagger.database.model.CompleteAnswer;
import io.swagger.database.model.User;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.LockModeType;
import java.util.List;

/**
 * Created by lux on 14.01.17.
 */
public interface AnswerRepository extends CrudRepository<CompleteAnswer, Long> {
}
