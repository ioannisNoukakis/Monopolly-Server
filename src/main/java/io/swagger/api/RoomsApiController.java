package io.swagger.api;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.cristallium.api.RoomsApi;
import com.cristallium.api.dto.*;
import com.cristallium.api.dto.CompletePoll;
import com.cristallium.api.dto.CompleteQuestion;
import com.cristallium.api.dto.CompleteRoom;
import io.swagger.annotations.ApiParam;
import io.swagger.database.dao.*;
import io.swagger.database.model.*;
import io.swagger.database.model.User;
import io.swagger.utils.Converter;
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
//TODO REVOIR LES PATHS POUR CEUX QUI NONT PAS BESOINS DE TOUT LES PARAMETRES
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

    private User getUserDB(String token)
    {
        long id;
        try{
            id = JWTutils.parseToken(token);
        }
        catch (JWTDecodeException e) {
            return null;
        }

        return userRepository.findOne(id);
    }

    /**
     * Post a room
     *
     * @param room
     * @param token
     * @return
     */
    @Override
    public ResponseEntity<Void> roomsPost(@ApiParam(value = "room to be created", required = true) @RequestBody Room room, @ApiParam(value = "token to be passed as a header", required = true) @RequestHeader(value = "token", required = true) String token) {

        User user = getUserDB(token);
        if(user==null)
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);

        if(room == null || room.getName() == null || room.getName().isEmpty())
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        roomRepository.save(new io.swagger.database.model.CompleteRoom(user, room.getName(), new LinkedList<io.swagger.database.model.CompletePoll>()));
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /**
     * Deletes a room
     *
     * @param roomId
     * @param token
     * @return
     */
    @Override
    public ResponseEntity<Void> roomsRoomIdDelete(@ApiParam(value = "room to be deleted", required = true) @PathVariable("roomId") Long roomId, @ApiParam(value = "token to be passed as a header", required = true) @RequestHeader(value = "token", required = true) String token) {

        User user = getUserDB(token);
        if(user==null)
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);

        io.swagger.database.model.CompleteRoom completeRoomDB = roomRepository.findOne(roomId);
        if(completeRoomDB == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        if(completeRoomDB.getOwner().getId() != user.getId())
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        roomRepository.delete(completeRoomDB);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * Gets a room
     *
     * @param roomId
     * @return
     */
    @Override
    public ResponseEntity<com.cristallium.api.dto.CompleteRoom> roomsRoomIdGet(@ApiParam(value = "room to be get", required = true) @PathVariable("roomId") Long roomId) {
        io.swagger.database.model.CompleteRoom completeRoomDB = roomRepository.findOne(roomId);

        if(completeRoomDB == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        com.cristallium.api.dto.CompleteRoom tmp = new com.cristallium.api.dto.CompleteRoom();
        tmp.setId(completeRoomDB.getId());
        tmp.setOwner(completeRoomDB.getOwner().getId());
        tmp.setName(completeRoomDB.getName());
        tmp.setPolls(Converter.pollsFromModelToDTO(completeRoomDB));

        return new ResponseEntity<>(tmp, HttpStatus.OK);
    }


    /**
     * Patches a room
     *
     * @param roomId
     * @param room
     * @param token
     * @return
     */
    @Override
    public ResponseEntity<Void> roomsRoomIdPatch(@ApiParam(value = "room to be modified", required = true) @PathVariable("roomId") Long roomId, @ApiParam(value = "room to be patched", required = true) @RequestBody Room room, @ApiParam(value = "token to be passed as a header", required = true) @RequestHeader(value = "token", required = true) String token) {

        User user = getUserDB(token);
        if(user==null)
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);

        io.swagger.database.model.CompleteRoom completeRoomDB = roomRepository.findOne(roomId);
        if(completeRoomDB == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        if(user.getId() != completeRoomDB.getOwner().getId())
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        if(room == null || room.getName() == null || room.getName().isEmpty())
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        completeRoomDB.setName(room.getName());

        roomRepository.save(completeRoomDB);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Deletes a poll in a room
     *
     * @param roomId
     * @param pId
     * @param token
     * @return
     */
    @Override
    public ResponseEntity<Void> roomsRoomIdPollsPIdDelete(@ApiParam(value = "the room", required = true) @PathVariable("roomId") Long roomId, @ApiParam(value = "the poll", required = true) @PathVariable("pId") Long pId, @ApiParam(value = "token to be passed as a header", required = true) @RequestHeader(value = "token", required = true) String token) {
        User user = getUserDB(token);
        if(user==null)
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);

        io.swagger.database.model.CompletePoll completePollDB = pollRepository.findOne(pId);
        if(completePollDB == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        if(completePollDB.getCompleteRoom().getOwner().getId() != user.getId())
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        pollRepository.delete(completePollDB);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * Creates a poll in a room
     *
     * @param roomId
     * @param poll
     * @param token
     * @return
     */
    @Override
    public ResponseEntity<Void> roomsRoomIdPost(@ApiParam(value = "the room", required = true) @PathVariable("roomId") Long roomId, @ApiParam(value = "poll to be created", required = true) @RequestBody Poll poll, @ApiParam(value = "token to be passed as a header", required = true) @RequestHeader(value = "token", required = true) String token) {
        User user = getUserDB(token);
        if(user==null)
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);

        io.swagger.database.model.CompleteRoom completeRoomDB = roomRepository.findOne(roomId);
        if(completeRoomDB == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        if(user.getId() != completeRoomDB.getOwner().getId())
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        if(poll == null || poll.getName() == null || poll.getName().isEmpty())
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        io.swagger.database.model.CompletePoll completePoll = new io.swagger.database.model.CompletePoll(poll.getName(), completeRoomDB, new LinkedList<io.swagger.database.model.CompleteQuestion>());
        pollRepository.save(completePoll);
        completeRoomDB.getPolls().add(completePoll);
        roomRepository.save(completeRoomDB);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /**
     * Gets a poll in a room
     *
     * @param roomId
     * @param pId
     * @return
     */
    @Override
    public ResponseEntity<CompletePoll> roomsRoomIdPollsPIdGet(@ApiParam(value = "the room", required = true) @PathVariable("roomId") Long roomId, @ApiParam(value = "poll to be retrived", required = true) @PathVariable("pId") Long pId) {
        io.swagger.database.model.CompletePoll completePollDB = pollRepository.findOne(pId);
        if(completePollDB == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        CompletePoll tmp = new CompletePoll();
        tmp.setId(completePollDB.getId());
        tmp.setName(completePollDB.getName());
        tmp.setQuestions(Converter.questionsFromModelToDTO(completePollDB));
        return new ResponseEntity<>(tmp, HttpStatus.OK);
    }

    /**
     * Patches a poll in a room
     *
     * @param roomId
     * @param pId
     * @param poll
     * @param token
     * @return
     */
    @Override
    public ResponseEntity<Void> roomsRoomIdPollsPIdPatch(@ApiParam(value = "the room", required = true) @PathVariable("roomId") Long roomId, @ApiParam(value = "poll to be retrived", required = true) @PathVariable("pId") Long pId, @ApiParam(value = "the poll", required = true) @RequestBody Poll poll, @ApiParam(value = "token to be passed as a header", required = true) @RequestHeader(value = "token", required = true) String token) {
        User user = getUserDB(token);
        if(user==null)
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);

        io.swagger.database.model.CompletePoll completePollDB = pollRepository.findOne(pId);
        if(completePollDB == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        if(user.getId() != completePollDB.getCompleteRoom().getOwner().getId())
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        if(poll == null || poll.getName() == null || poll.getName().isEmpty())
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        completePollDB.setName(poll.getName());

        pollRepository.save(completePollDB);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Creates a question and it's answers
     * @param roomId
     * @param pId
     * @param question
     * @param token
     * @return
     */
    @Override
    public ResponseEntity<Void> roomsRoomIdPollsPIdPost(@ApiParam(value = "the room", required = true) @PathVariable("roomId") Long roomId, @ApiParam(value = "the poll", required = true) @PathVariable("pId") Long pId, @ApiParam(value = "Question to be added", required = true) @RequestBody Question question, @ApiParam(value = "token to be passed as a header", required = true) @RequestHeader(value = "token", required = true) String token) {
        User user = getUserDB(token);
        if(user==null)
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);

        io.swagger.database.model.CompletePoll completePoll = pollRepository.findOne(pId);
        if(completePoll == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        if(user.getId() != completePoll.getCompleteRoom().getOwner().getId())
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        if(question == null || question.getBody() == null || question.getBody().isEmpty() || question.getAnswers() == null || question.getAnswers().isEmpty())
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        LinkedList<CompleteAnswer> completeAnswers = new LinkedList<>();

        for(Asnwer asnwer : question.getAnswers())
        {
            //TODO Verifier les answers
            CompleteAnswer tmp = new CompleteAnswer();
            tmp.setBody(asnwer.getBody());
            tmp.setValid(asnwer.getIsValid());
            answerRepository.save(tmp);
            completeAnswers.push(tmp);
        }

        io.swagger.database.model.CompleteQuestion completeQuestion = new io.swagger.database.model.CompleteQuestion(question.getBody(), completePoll, completeAnswers);
        questionReporitory.save(completeQuestion);
        completePoll.getQuestions().add(completeQuestion);
        pollRepository.save(completePoll);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }


    @Override
    public ResponseEntity<Void> roomsRoomIdPollsPIdQuestionQIdDelete(@ApiParam(value = "the room", required = true) @PathVariable("roomId") Long roomId, @ApiParam(value = "poll to be patched", required = true) @PathVariable("pId") Long pId, @ApiParam(value = "poll to be retrived", required = true) @PathVariable("qId") Long qId, @ApiParam(value = "token to be passed as a header", required = true) @RequestHeader(value = "token", required = true) String token) {
        User user = getUserDB(token);
        if(user==null)
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);

        io.swagger.database.model.CompleteQuestion completeQuestion = questionReporitory.findOne(qId);
        if(completeQuestion == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        if(completeQuestion.getCompletePoll().getCompleteRoom().getOwner().getId() != user.getId())
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        questionReporitory.delete(completeQuestion);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<Void> roomsRoomIdPollsPIdQuestionQIdPatch(@ApiParam(value = "the room", required = true) @PathVariable("roomId") Long roomId, @ApiParam(value = "the poll", required = true) @PathVariable("pId") Long pId, @ApiParam(value = "the question", required = true) @PathVariable("qId") Long qId, @ApiParam(value = "question to be patched", required = true) @RequestBody com.cristallium.api.dto.CompleteQuestion question, @ApiParam(value = "token to be passed as a header", required = true) @RequestHeader(value = "token", required = true) String token) {
        User user = getUserDB(token);
        if(user==null)
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);

        io.swagger.database.model.CompleteQuestion completeQuestion = questionReporitory.findOne(qId);
        if(completeQuestion == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        if(completeQuestion.getCompletePoll().getCompleteRoom().getOwner().getId() != user.getId())
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        if(question == null || question.getBody() == null || question.getBody().isEmpty() || question.getAnswers() == null || question.getAnswers().isEmpty())
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        completeQuestion.setAnswers(new LinkedList<CompleteAnswer>());

        for(CompleteAsnwer asnwer : question.getAnswers())
        {
            //TODO Verifier les answers
            CompleteAnswer tmp = new CompleteAnswer();
            tmp.setBody(asnwer.getBody());
            tmp.setValid(asnwer.getIsValid());
            answerRepository.save(tmp);
            completeQuestion.getAnswers().add(tmp);
        }

        questionReporitory.save(completeQuestion);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
