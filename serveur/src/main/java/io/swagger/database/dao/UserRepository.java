package io.swagger.database.dao;

import io.swagger.database.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * Created by lux on 10.12.16.
 */
@Transactional
public interface UserRepository extends CrudRepository<User, Long> {
    User findByUsername(String username);
}
