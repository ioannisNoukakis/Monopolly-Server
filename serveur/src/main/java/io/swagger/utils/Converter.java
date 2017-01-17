package io.swagger.utils;

import com.cristallium.api.dto.CompleteAsnwer;
import com.cristallium.api.dto.CompletePoll;
import io.swagger.database.model.CompleteAnswer;
import io.swagger.database.model.CompleteQuestion;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by durza9390 on 15.01.2017.
 */
public class Converter {

    public static List<com.cristallium.api.dto.CompleteQuestion> questionsFromModelToDTO(io.swagger.database.model.CompleteRoom completeRoom, boolean hideAnswers)
    {
        LinkedList<com.cristallium.api.dto.CompleteQuestion> completeQuestions = new LinkedList<>();
        for(CompleteQuestion completeQuestion: completeRoom.getQuestions())
        {
            com.cristallium.api.dto.CompleteQuestion tmp = new com.cristallium.api.dto.CompleteQuestion();
            tmp.setId(completeQuestion.getId());
            tmp.setBody(completeQuestion.getBody());
            tmp.setClosed(completeQuestion.isClosed());
            tmp.setAnswers(answersFromModelToDTO(completeQuestion, hideAnswers));
            completeQuestions.push(tmp);
        }
        return completeQuestions;
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
}
