package io.swagger.api;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.cristallium.api.RoomsApi;
import com.cristallium.api.dto.*;
import io.swagger.DataWatcher.DataWatcher;
import io.swagger.annotations.ApiParam;
import io.swagger.database.dao.*;
import io.swagger.database.model.*;
import io.swagger.database.model.CompleteQuestion;
import io.swagger.database.model.User;
import io.swagger.utils.Converter;
import io.swagger.utils.JSONParser;
import io.swagger.utils.JWTutils;
import io.swagger.websocket.dto.reply.QuestionReply;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
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
    public synchronized ResponseEntity<Void> roomsPost(@ApiParam(value = "room to be created", required = true) @RequestBody Room room, @ApiParam(value = "token to be passed as a header", required = true) @RequestHeader(value = "token", required = true) String token) {

        User user = getUserDB(token);
        if(user==null)
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);

        if(room == null || room.getName() == null || room.getName().isEmpty())
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        try {
            roomRepository.save(new io.swagger.database.model.CompleteRoom(user, room.getName(), new LinkedList<CompleteQuestion>(), new LinkedList<User>()));
        }catch (DataIntegrityViolationException e)
        {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
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
    public synchronized ResponseEntity<Void> roomsRoomIdDelete(@ApiParam(value = "room to be deleted", required = true) @PathVariable("roomId") Long roomId, @ApiParam(value = "token to be passed as a header", required = true) @RequestHeader(value = "token", required = true) String token) {

        User user = getUserDB(token);
        if(user==null)
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);

        io.swagger.database.model.CompleteRoom completeRoomDB = roomRepository.findOne(roomId);
        if(completeRoomDB == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        if(completeRoomDB.getOwner().getId() != user.getId())
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        for(CompleteQuestion completeQuestion: completeRoomDB.getQuestions())
        {
            for(CompleteAnswer completeAnswer: completeQuestion.getAnswers())
            {
                completeAnswer.setCompleteQuestion(null);
                completeAnswer.setUsers(new LinkedList<User>());
                answerRepository.delete(completeAnswer);
            }
            completeQuestion.setAnswers(new LinkedList<CompleteAnswer>());
            completeQuestion.setCompleteRoom(null);
            questionReporitory.delete(completeQuestion);
        }
        completeRoomDB.setQuestions(new LinkedList<CompleteQuestion>());
        completeRoomDB.setOwner(null);
        completeRoomDB.setSubscribers(new LinkedList<User>());
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
        tmp.setQuestions(Converter.questionsFromModelToDTO(completeRoomDB, null, true));

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
    public synchronized ResponseEntity<Void> roomsRoomIdPatch(@ApiParam(value = "room to be modified", required = true) @PathVariable("roomId") Long roomId, @ApiParam(value = "room to be patched", required = true) @RequestBody Room room, @ApiParam(value = "token to be passed as a header", required = true) @RequestHeader(value = "token", required = true) String token) {

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

    @Override
    public synchronized ResponseEntity<com.cristallium.api.dto.CompleteQuestion> roomsRoomIdPost(@ApiParam(value = "the room", required = true) @PathVariable("roomId") Long roomId, @ApiParam(value = "Question to be added", required = true) @RequestBody Question question, @ApiParam(value = "token to be passed as a header", required = true) @RequestHeader(value = "token", required = true) String token) {
        User user = getUserDB(token);
        if(user==null)
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);

        io.swagger.database.model.CompleteRoom completeRoom = roomRepository.findOne(roomId);
        if(completeRoom == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        if(user.getId() != completeRoom.getOwner().getId())
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        if(question == null || question.getBody() == null || question.getBody().isEmpty() || question.getAnswers() == null || question.getAnswers().isEmpty())
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        io.swagger.database.model.CompleteQuestion completeQuestion = new io.swagger.database.model.CompleteQuestion(question.getBody(), false, completeRoom, new LinkedList<CompleteAnswer>());
        questionReporitory.save(completeQuestion);
        completeRoom.getQuestions().add(completeQuestion);
        roomRepository.save(completeRoom);

        LinkedList<CompleteAnswer> completeAnswers = new LinkedList<>();

        for(Asnwer asnwer : question.getAnswers())
        {
            //TODO Verifier les answers
            CompleteAnswer tmp = new CompleteAnswer();
            tmp.setBody(asnwer.getBody());
            tmp.setValid(asnwer.getIsValid());
            tmp.setCompleteQuestion(completeQuestion);
            answerRepository.save(tmp);
            completeAnswers.push(tmp);
        }
        completeQuestion.setAnswers(completeAnswers);
        completeQuestion = questionReporitory.save(completeQuestion);

        com.cristallium.api.dto.CompleteQuestion questionDTO = new com.cristallium.api.dto.CompleteQuestion();
        questionDTO.setBody(completeQuestion.getBody());
        questionDTO.setId(completeQuestion.getId());
        questionDTO.setAnswers(Converter.answersFromModelToDTO(completeQuestion, false));
        questionDTO.setClosed(completeQuestion.isClosed());
        questionDTO.setCanAnswer(Converter.canAnswer(user,completeQuestion));

        DataWatcher.getInstance().notifyClients(JSONParser.toJson(new QuestionReply(questionDTO)), completeQuestion.getCompleteRoom().getId());

        return new ResponseEntity<>(questionDTO, HttpStatus.CREATED);
    }

    @Override
    public synchronized ResponseEntity<Void> roomsQuestionQIdDelete(@ApiParam(value = "poll to be deleted", required = true) @PathVariable("qId") Long qId, @ApiParam(value = "token to be passed as a header", required = true) @RequestHeader(value = "token", required = true) String token) {
        User user = getUserDB(token);
        if(user==null)
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);

        io.swagger.database.model.CompleteQuestion completeQuestion = questionReporitory.findOne(qId);
        if(completeQuestion == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        if(completeQuestion.getCompleteRoom().getOwner().getId() != user.getId())
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        for(CompleteAnswer completeAnswer: completeQuestion.getAnswers())
        {
            completeAnswer.setCompleteQuestion(null);
            completeAnswer.setUsers(new LinkedList<User>());
            answerRepository.delete(completeAnswer);
        }
        completeQuestion.setAnswers(new LinkedList<CompleteAnswer>());
        completeQuestion.getCompleteRoom().getQuestions().remove(completeQuestion);
        completeQuestion.setCompleteRoom(null);
        questionReporitory.delete(completeQuestion);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<StatisticalQuestion> roomsQuestionQIdGet(@ApiParam(value = "question to be retrived", required = true) @PathVariable("qId") Long qId) {

        io.swagger.database.model.CompleteQuestion completeQuestion = questionReporitory.findOne(qId);
        if(completeQuestion == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        StatisticalQuestion statisticalQuestion = new StatisticalQuestion();
        statisticalQuestion.setId(completeQuestion.getId());
        statisticalQuestion.setBody(completeQuestion.getBody());
        statisticalQuestion.setClosed(completeQuestion.isClosed());
        statisticalQuestion.setAnswers(Converter.statisticalAnswersFromModelToDTO(completeQuestion));

        return new ResponseEntity<>(statisticalQuestion, HttpStatus.OK);
    }


    @Override
    public synchronized ResponseEntity<Void> roomsQuestionQIdPatch(@ApiParam(value = "the question", required = true) @PathVariable("qId") Long qId, @ApiParam(value = "question to be patched", required = true) @RequestBody com.cristallium.api.dto.CompleteQuestion question, @ApiParam(value = "token to be passed as a header", required = true) @RequestHeader(value = "token", required = true) String token) {
        User user = getUserDB(token);
        if(user==null)
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);

        io.swagger.database.model.CompleteQuestion completeQuestion = questionReporitory.findOne(qId);
        if(completeQuestion == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        if(completeQuestion.getCompleteRoom().getOwner().getId() != user.getId())
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        if(question == null || question.getBody() == null || question.getBody().isEmpty() || question.getAnswers() == null || question.getAnswers().isEmpty())
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        completeQuestion.setBody(question.getBody());
        completeQuestion.setClosed(question.getClosed());

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
