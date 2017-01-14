package io.swagger.api;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.cristallium.api.UserApi;
import com.cristallium.api.dto.CompleteAsnwer;
import com.cristallium.api.dto.CompletePoll;
import com.cristallium.api.dto.CompleteRoom;
import com.cristallium.api.dto.User;
import io.swagger.annotations.ApiParam;
import io.swagger.database.dao.RoomRepository;
import io.swagger.database.dao.UserRepository;
import io.swagger.database.model.CompleteAnswer;
import io.swagger.database.model.CompleteQuestion;
import io.swagger.utils.JWTutils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.ArrayList;
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
    public ResponseEntity<Void> userDelete(@ApiParam(value = "token to be passed as a header", required = true) @RequestHeader(value = "token", required = true) String token) {
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
        if(user.getUsername().isEmpty() || user.getPassword().isEmpty())
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        try {
            userRepository.save(new io.swagger.database.model.User(user.getUsername(), user.getPassword()));
        }catch (DataIntegrityViolationException e)
        {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<List<CompleteRoom>> userRoomsGet(@ApiParam(value = "token to be passed as a header", required = true) @RequestHeader(value = "token", required = true) String token) {
        long id = -1;
        try{
            id = JWTutils.parseToken(token);
        }
        catch (JWTDecodeException e) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        LinkedList<CompleteRoom> completeRooms = new LinkedList<>();
        for(io.swagger.database.model.CompleteRoom completeRoom : roomRepository.getByOwner(userRepository.findOne(id)))
        {
            CompleteRoom tmp = new CompleteRoom();
            tmp.setId(completeRoom.getId());
            tmp.setOwner(completeRoom.getOwner().getId());
            tmp.setName(completeRoom.getName());
            tmp.setPolls(pollsFromModelToDTO(completeRoom));
            completeRooms.push(tmp);
        }

        return new ResponseEntity<>((List<CompleteRoom>)completeRooms, HttpStatus.OK);
    }

    private List<CompletePoll> pollsFromModelToDTO(io.swagger.database.model.CompleteRoom completeRoom)
    {
        LinkedList<CompletePoll> completePolls = new LinkedList<>();
        for(io.swagger.database.model.CompletePoll completePoll : completeRoom.getPolls())
        {
            CompletePoll tmp = new CompletePoll();
            tmp.setId(completePoll.getId());
            tmp.setName(completePoll.getName());
            tmp.setQuestions(questionsFromModelToDTO(completePoll));
            completePolls.push(tmp);
        }
        return completePolls;
    }

    private List<com.cristallium.api.dto.CompleteQuestion> questionsFromModelToDTO(io.swagger.database.model.CompletePoll completePoll)
    {
        LinkedList<com.cristallium.api.dto.CompleteQuestion> completeQuestions = new LinkedList<>();
        for(CompleteQuestion completeQuestion: completePoll.getQuestions())
        {
            com.cristallium.api.dto.CompleteQuestion tmp = new com.cristallium.api.dto.CompleteQuestion();
            tmp.setId(completeQuestion.getId());
            tmp.setBody(completeQuestion.getBody());
            tmp.setAnswers(answersFromModelToDTO(completeQuestion, false));
            completeQuestions.push(tmp);
        }
        return completeQuestions;
    }

    /**
     * CAUTION! This method return the correct answer of the question. Use the parameter to true to disable that
     * @param completeQuestion
     * @return
     */
    private List<CompleteAsnwer> answersFromModelToDTO(CompleteQuestion completeQuestion, boolean hideAnswers)
    {
        LinkedList<CompleteAsnwer> completeAsnwers = new LinkedList<>();
        for(CompleteAnswer completeAnswer : completeQuestion.getAnswers())
        {
            CompleteAsnwer tmp = new CompleteAsnwer();
            tmp.setId(completeAnswer.getId());
            tmp.setBody(tmp.getBody());
            if(hideAnswers)
                tmp.setIsValid(false);
            else
                tmp.setIsValid(tmp.getIsValid());
            completeAsnwers.push(tmp);
        }
        return completeAsnwers;
    }
}
