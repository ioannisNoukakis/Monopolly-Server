package io.swagger.api;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.cristallium.api.UserApi;
import com.cristallium.api.dto.CompleteRoom;
import com.cristallium.api.dto.User;
import io.swagger.annotations.ApiParam;
import io.swagger.database.dao.RoomRepository;
import io.swagger.database.dao.UserRepository;
import io.swagger.database.model.CompleteAnswer;
import io.swagger.utils.Converter;
import io.swagger.utils.JWTutils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by lux on 13.01.17.
 */
@Controller
public class UserApiController implements UserApi {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoomRepository roomRepository;
    /**
     * Delete a user endpoint.
     *
     * Tested by Ioannis Noukakis 13.01.2017
     *
     * @param token
     * @return
     */
    @Override
    public synchronized ResponseEntity<Void> userDelete(@ApiParam(value = "token to be passed as a header", required = true) @RequestHeader(value = "token", required = true) String token) {
        long id = -1;
        try{
            id = JWTutils.parseToken(token);
        }
        catch (JWTDecodeException e) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        try {
            userRepository.delete(id);
        }catch (DataIntegrityViolationException e)
        {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * User creation endpoint.
     *
     * Tested the 13.01.2017 by Ioannis Noukakis
     *
     * @param user user to be created.
     * @return
     */
    @Override
    public ResponseEntity<Void> userPost(@ApiParam(value = "user to be created", required = true) @RequestBody User user) {
        if(user == null || user.getUsername() == null || user.getPassword() == null || user.getUsername().isEmpty() || user.getPassword().isEmpty())
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        try {
            userRepository.save(new io.swagger.database.model.User(user.getUsername(), user.getPassword(), new LinkedList<io.swagger.database.model.CompleteRoom>(), new LinkedList<CompleteAnswer>(), new LinkedList<io.swagger.database.model.CompleteRoom>()));
        }catch (DataIntegrityViolationException e)
        {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Override
    public synchronized ResponseEntity<List<CompleteRoom>> userRoomsGet(@ApiParam(value = "token to be passed as a header", required = true) @RequestHeader(value = "token", required = true) String token) {
        long id;
        try{
            id = JWTutils.parseToken(token);
        }
        catch (JWTDecodeException e) {
            return null;
        }
        io.swagger.database.model.User userDB = userRepository.findOne(id);

        if(userDB==null)
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);

        LinkedList<CompleteRoom> completeRooms = new LinkedList<>();
        completeRooms = addToList(completeRooms, userDB, roomRepository.findByOwner(userRepository.findOne(id)));
        completeRooms = addToList(completeRooms, userDB, userDB.getSubscribed());
        return new ResponseEntity<>((List<CompleteRoom>)completeRooms, HttpStatus.OK);
    }

    private LinkedList<CompleteRoom> addToList(LinkedList<CompleteRoom> completeRooms, io.swagger.database.model.User user, List<io.swagger.database.model.CompleteRoom> b)
    {
        for(io.swagger.database.model.CompleteRoom completeRoom : b)
        {
            CompleteRoom tmp = new CompleteRoom();
            tmp.setId(completeRoom.getId());
            tmp.setOwner(completeRoom.getOwner().getId());
            tmp.setName(completeRoom.getName());
            tmp.setQuestions(Converter.questionsFromModelToDTO(completeRoom, user , false));
            completeRooms.push(tmp);
        }
        return completeRooms;
    }
}
