package io.swagger.api;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.cristallium.api.AnswerApi;
import com.cristallium.api.dto.CompleteAsnwer;
import com.cristallium.api.dto.CompleteQuestion;
import io.swagger.annotations.ApiParam;
import io.swagger.database.dao.AnswerRepository;
import io.swagger.database.dao.UserRepository;
import io.swagger.database.model.CompleteAnswer;
import io.swagger.database.model.User;
import io.swagger.utils.Converter;
import io.swagger.utils.JWTutils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
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

    @Override
    public ResponseEntity<CompleteQuestion> answerPost(@ApiParam(value = "The answer to be submitted.", required = true) @RequestBody CompleteAsnwer answer, @ApiParam(value = "token to be passed as a header", required = true) @RequestHeader(value = "token", required = true) String token) {
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

        CompleteAnswer completeAnswerDB = answerRepository.findOne(answer.getId());
        if(completeAnswerDB == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        if(completeAnswerDB.getCompleteQuestion().isClosed())
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        if(answer == null || answer.getIsValid() == null || answer.getBody() == null || answer.getBody().isEmpty())
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        //on vérifie que l'utilisateur n'as pas déjà répondu à cette question
        for(CompleteAnswer cAnswer : answerRepository.getAnswerByUser(userDB))
        {
            if(cAnswer.getUser().contains(userDB))
                return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        userDB.getAnswers().add(completeAnswerDB);
        completeAnswerDB.getUser().add(userDB);

        userRepository.save(userDB);
        answerRepository.save(completeAnswerDB);

        CompleteQuestion completeQuestion = new CompleteQuestion();
        completeQuestion.setBody(completeAnswerDB.getCompleteQuestion().getBody());
        completeQuestion.setClosed(completeAnswerDB.getCompleteQuestion().isClosed());
        completeQuestion.setAnswers(Converter.answersFromModelToDTO(completeAnswerDB.getCompleteQuestion(), false));

        return new ResponseEntity<>(completeQuestion, HttpStatus.OK);
    }
}
