package io.swagger.api;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.cristallium.api.SubscribeApi;
import io.swagger.annotations.ApiParam;
import io.swagger.database.dao.RoomRepository;
import io.swagger.database.dao.UserRepository;
import io.swagger.database.model.CompleteRoom;
import io.swagger.database.model.User;
import io.swagger.utils.JWTutils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 * Created by durza9390 on 17.01.2017.
 */
@Controller
public class SubscribeApiController implements SubscribeApi {

    @Autowired
    RoomRepository roomRepository;

    @Autowired
    UserRepository userRepository;

    @Override
    public synchronized ResponseEntity<Void> subscribeRoomNamePost(@ApiParam(value = "the room to join.", required = true) @PathVariable("roomName") String roomName, @ApiParam(value = "token to be passed as a header", required = true) @RequestHeader(value = "token", required = true) String token) {

        long id;
        try{
            id = JWTutils.parseToken(token);
        }
        catch (JWTDecodeException e) {
            return null;
        }
        User userDB = userRepository.findOne(id);

        if(userDB==null)
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);

        CompleteRoom completeRoom = roomRepository.findByName(roomName);
        if(completeRoom == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        if(userDB.getSubscribed().contains(completeRoom) || completeRoom.getOwner().getId() == userDB.getId())
            return new ResponseEntity<>(HttpStatus.CONFLICT);

        userDB.getSubscribed().add(completeRoom);
        completeRoom.getSubscribers().add(userDB);

        userRepository.save(userDB);
        roomRepository.save(completeRoom);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
