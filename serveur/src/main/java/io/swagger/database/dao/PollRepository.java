package io.swagger.database.dao;

import io.swagger.database.model.CompletePoll;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by lux on 14.01.17.
 */
public interface PollRepository extends CrudRepository<CompletePoll, Long> {
}
