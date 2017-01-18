package io.swagger.websocket.dto.reply;

import com.cristallium.api.dto.CompleteQuestion;

/**
 * Created by durza9390 on 18.01.2017.
 */
public class QuestionReply extends BaseReply {
    private CompleteQuestion completeQuestion;

    public QuestionReply(CompleteQuestion completeQuestion) {
        super("addQuestion");
        this.completeQuestion = completeQuestion;
    }

    public CompleteQuestion getCompleteQuestion() {
        return completeQuestion;
    }

    public void setCompleteQuestion(CompleteQuestion completeQuestion) {
        this.completeQuestion = completeQuestion;
    }
}
