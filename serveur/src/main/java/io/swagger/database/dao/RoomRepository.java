package io.swagger.database.dao;

import io.swagger.database.model.CompleteRoom;
import io.swagger.database.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by lux on 14.01.17.
 */
public interface RoomRepository extends CrudRepository<CompleteRoom, Long> {
    List<CompleteRoom> findByOwner(User user);
    CompleteRoom findByName(String name);
}
