package io.swagger.utils;

import com.cristallium.api.dto.CompleteAsnwer;
import com.cristallium.api.dto.StatisticalAnswer;
import com.cristallium.api.dto.UserDTO;
import io.swagger.database.model.CompleteAnswer;
import io.swagger.database.model.CompleteQuestion;
import io.swagger.database.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by durza9390 on 15.01.2017.
 */
public class Converter {

    public static List<com.cristallium.api.dto.CompleteQuestion> questionsFromModelToDTO(io.swagger.database.model.CompleteRoom completeRoom, User user, boolean hideAnswers)
    {
        LinkedList<com.cristallium.api.dto.CompleteQuestion> completeQuestions = new LinkedList<>();
        for(CompleteQuestion completeQuestion: completeRoom.getQuestions())
        {
            com.cristallium.api.dto.CompleteQuestion tmp = new com.cristallium.api.dto.CompleteQuestion();
            tmp.setId(completeQuestion.getId());
            tmp.setBody(completeQuestion.getBody());
            tmp.setClosed(completeQuestion.isClosed());
            tmp.setAnswers(answersFromModelToDTO(completeQuestion, hideAnswers));
            if(user != null) {
                tmp.setCanAnswer(canAnswer(user, completeQuestion));
            }
            completeQuestions.push(tmp);
        }
        return completeQuestions;
    }

    public static boolean canAnswer(User user, CompleteQuestion completeQuestion)
    {
            boolean can = true;
            for(CompleteAnswer cAnswer : user.getAnswers()) //on charge les réponses que l'utilisateur à déjà entré
            {
                if (cAnswer.getCompleteQuestion().getId() == completeQuestion.getId()) {//on vérifie que l'utilisateur n'as pas déjà répondu à cette question
                    return false;
                }
            }
        return true;
    }

    /**
     * CAUTION! This method return the correct answer of the question. Use the parameter to true to disable that
     * @param completeQuestion
     * @return
     */
    public static List<CompleteAsnwer> answersFromModelToDTO(CompleteQuestion completeQuestion, boolean hideAnswers)
    {
        LinkedList<CompleteAsnwer> completeAsnwers = new LinkedList<>();
        for(CompleteAnswer completeAnswer : completeQuestion.getAnswers())
        {
            CompleteAsnwer tmp = new CompleteAsnwer();
            tmp.setId(completeAnswer.getId());
            tmp.setBody(completeAnswer.getBody());
            if(hideAnswers)
                tmp.setIsValid(false);
            else
                tmp.setIsValid(completeAnswer.isValid());
            completeAsnwers.push(tmp);
        }
        return completeAsnwers;
    }

    public static List<StatisticalAnswer> statisticalAnswersFromModelToDTO(CompleteQuestion completeQuestion)
    {
        List<StatisticalAnswer> statisticalAnswers = new LinkedList<>();
        for(CompleteAnswer completeAnswer: completeQuestion.getAnswers())
        {
            StatisticalAnswer tmp = new StatisticalAnswer();
            tmp.setId(completeAnswer.getId());
            tmp.setBody(completeAnswer.getBody());
            tmp.setIsValid(completeAnswer.isValid());

            List<UserDTO> userDTOs = new LinkedList<>();
            for(User user : completeAnswer.getUsers())
            {
                UserDTO u = new UserDTO();
                u.setId(user.getId());
                u.setUsername(user.getUsername());
                userDTOs.add(u);
            }
            tmp.setUsers(userDTOs);
            statisticalAnswers.add(tmp);
        }
        return statisticalAnswers;
    }
}
