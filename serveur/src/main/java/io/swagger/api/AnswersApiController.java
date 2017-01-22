package io.swagger.api;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.cristallium.api.AnswerApi;
import com.cristallium.api.dto.CompleteAsnwer;
import com.cristallium.api.dto.CompleteQuestion;
import com.cristallium.api.dto.StatisticalQuestion;
import io.swagger.DataWatcher.DataWatcher;
import io.swagger.annotations.ApiParam;
import io.swagger.database.dao.AnswerRepository;
import io.swagger.database.dao.UserRepository;
import io.swagger.database.model.CompleteAnswer;
import io.swagger.database.model.User;
import io.swagger.utils.Converter;
import io.swagger.utils.JSONParser;
import io.swagger.utils.JWTutils;
import io.swagger.websocket.dto.reply.UserAnswer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 * Created by lux on 16.01.17.
 */
@Controller
public class AnswersApiController implements AnswerApi {

    @Autowired
    AnswerRepository answerRepository;

    @Autowired
    UserRepository userRepository;

    private void notifyUserForGivenAnswer(String username, Long questionId, Long userAnswerId, Long roomId)
    {
        DataWatcher.getInstance().notifyClients(JSONParser.toJson(new UserAnswer(username, questionId, userAnswerId)), roomId);
    }

    @Override
    public ResponseEntity<CompleteQuestion> answerAIdPost(@ApiParam(value = "the id of the answer", required = true) @PathVariable("aId") Long aId, @ApiParam(value = "token to be passed as a header", required = true) @RequestHeader(value = "token", required = true) String token) {
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

        CompleteAnswer completeAnswerDB = answerRepository.findOne(aId);
        if(completeAnswerDB == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        if(completeAnswerDB.getCompleteQuestion().isClosed())
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        if(completeAnswerDB.getCompleteQuestion().isClosed())
            return new ResponseEntity<>(HttpStatus.CONFLICT);

        for(CompleteAnswer cAnswer : userDB.getAnswers()) //on charge les réponses que l'utilisateur à déjà entré
        {
            if (cAnswer.getCompleteQuestion().getId() == completeAnswerDB.getCompleteQuestion().getId())//on vérifie que l'utilisateur n'as pas déjà répondu à cette question
                return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        userDB.getAnswers().add(completeAnswerDB);
        completeAnswerDB.getUsers().add(userDB);

        userRepository.save(userDB);
        answerRepository.save(completeAnswerDB);

        CompleteQuestion completeQuestion = new CompleteQuestion();
        completeQuestion.setBody(completeAnswerDB.getCompleteQuestion().getBody());
        completeQuestion.setClosed(completeAnswerDB.getCompleteQuestion().isClosed());
        completeQuestion.setAnswers(Converter.answersFromModelToDTO(completeAnswerDB.getCompleteQuestion(), false));
        completeQuestion.setCanAnswer(false);



        notifyUserForGivenAnswer(userDB.getUsername(), completeAnswerDB.getCompleteQuestion().getId(), completeAnswerDB.getId(), completeAnswerDB.getCompleteQuestion().getCompleteRoom().getId());
        return new ResponseEntity<>(completeQuestion, HttpStatus.OK);
    }
}
