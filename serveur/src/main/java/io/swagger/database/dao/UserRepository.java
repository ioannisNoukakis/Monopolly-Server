package io.swagger.database.dao;

import io.swagger.database.model.User;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.LockModeType;
import java.util.List;


/**
 * Created by lux on 10.12.16.
 */
public interface UserRepository extends CrudRepository<User, Long> {

    User findByUsername(String username);
}
