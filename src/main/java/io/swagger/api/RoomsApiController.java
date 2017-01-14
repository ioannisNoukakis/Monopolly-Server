package io.swagger.api;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.cristallium.api.RoomsApi;
import com.cristallium.api.dto.CompletePoll;
import com.cristallium.api.dto.Poll;
import com.cristallium.api.dto.Question;
import com.cristallium.api.dto.Room;
import io.swagger.annotations.ApiParam;
import io.swagger.database.dao.*;
import io.swagger.database.model.CompleteRoom;
import io.swagger.database.model.User;
import io.swagger.utils.JWTutils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.LinkedList;

/**
 * Created by lux on 14.01.17.
 */
@Controller
public class RoomsApiController implements RoomsApi {

    @Autowired
    RoomRepository roomRepository;

    @Autowired
    PollRepository pollRepository;

    @Autowired
    QuestionReporitory questionReporitory;

    @Autowired
    AnswerRepository answerRepository;

    @Autowired
    UserRepository userRepository;


    @Override
    public ResponseEntity<Void> roomsPost(@ApiParam(value = "room to be created", required = true) @RequestBody Room room, @ApiParam(value = "token to be passed as a header", required = true) @RequestHeader(value = "token", required = true) String token) {
        long id = -1;
        User user = null;
        try{
            id = JWTutils.parseToken(token);
        }
        catch (JWTDecodeException e) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        user = userRepository.findOne(id);

        if(room == null || room.getName() == null || room.getName().isEmpty())
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        roomRepository.save(new CompleteRoom(user, room.getName(), new LinkedList<io.swagger.database.model.CompletePoll>()));
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Void> roomsRoomIdDelete(@ApiParam(value = "room to be deleted", required = true) @PathVariable("roomId") Long roomId, @ApiParam(value = "token to be passed as a header", required = true) @RequestHeader(value = "token", required = true) String token) {
        return null;
    }

    @Override
    public ResponseEntity<Void> roomsRoomIdGet(@ApiParam(value = "room to be get", required = true) @PathVariable("roomId") Long roomId, @ApiParam(value = "token to be passed as a header", required = true) @RequestHeader(value = "token", required = true) String token) {
        return null;
    }

    @Override
    public ResponseEntity<Void> roomsRoomIdPatch(@ApiParam(value = "room to be modified", required = true) @PathVariable("roomId") Long roomId, @ApiParam(value = "room to be patched", required = true) @RequestBody Room room, @ApiParam(value = "token to be passed as a header", required = true) @RequestHeader(value = "token", required = true) String token) {
        return null;
    }

    @Override
    public ResponseEntity<Void> roomsRoomIdPollsPIdDelete(@ApiParam(value = "the room", required = true) @PathVariable("roomId") Long roomId, @ApiParam(value = "poll to be retrived", required = true) @PathVariable("pId") Long pId, @ApiParam(value = "token to be passed as a header", required = true) @RequestHeader(value = "token", required = true) String token) {
        return null;
    }

    @Override
    public ResponseEntity<CompletePoll> roomsRoomIdPollsPIdGet(@ApiParam(value = "the room", required = true) @PathVariable("roomId") Long roomId, @ApiParam(value = "poll to be retrived", required = true) @PathVariable("pId") Long pId) {
        return null;
    }

    @Override
    public ResponseEntity<Void> roomsRoomIdPollsPIdPost(@ApiParam(value = "the room", required = true) @PathVariable("roomId") Long roomId, @ApiParam(value = "poll to be modified", required = true) @PathVariable("pId") Long pId, @ApiParam(value = "poll to be patched", required = true) @RequestBody Question poll, @ApiParam(value = "token to be passed as a header", required = true) @RequestHeader(value = "token", required = true) String token) {
        return null;
    }

    @Override
    public ResponseEntity<Void> roomsRoomIdPollsPIdQuestionQIdDelete(@ApiParam(value = "the room", required = true) @PathVariable("roomId") Long roomId, @ApiParam(value = "poll to be patched", required = true) @PathVariable("pId") Long pId, @ApiParam(value = "poll to be retrived", required = true) @PathVariable("qId") Long qId, @ApiParam(value = "token to be passed as a header", required = true) @RequestHeader(value = "token", required = true) String token) {
        return null;
    }

    @Override
    public ResponseEntity<Void> roomsRoomIdPollsPIdQuestionQIdPatch(@ApiParam(value = "the room", required = true) @PathVariable("roomId") Long roomId, @ApiParam(value = "poll to be patched", required = true) @PathVariable("pId") Long pId, @ApiParam(value = "poll to be retrived", required = true) @PathVariable("qId") Long qId, @ApiParam(value = "poll to be patched", required = true) @RequestBody CompletePoll poll, @ApiParam(value = "token to be passed as a header", required = true) @RequestHeader(value = "token", required = true) String token) {
        return null;
    }

    @Override
    public ResponseEntity<Void> roomsRoomIdPollsPost(@ApiParam(value = "the room", required = true) @PathVariable("roomId") Long roomId, @ApiParam(value = "poll to be created", required = true) @RequestBody Poll poll, @ApiParam(value = "token to be passed as a header", required = true) @RequestHeader(value = "token", required = true) String token) {
        return null;
    }
}
