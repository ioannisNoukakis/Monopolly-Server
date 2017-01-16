package io.swagger.database.dao;

import io.swagger.database.model.CompletePoll;
import io.swagger.database.model.CompleteRoom;
import io.swagger.database.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by lux on 14.01.17.
 */
public interface RoomRepository extends CrudRepository<CompleteRoom, Long> {
    List<CompleteRoom> getByOwner(User user);
}
