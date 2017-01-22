package io.swagger.websocket.dto.reply;

import com.cristallium.api.dto.StatisticalQuestion;

/**
 * Created by lux on 17.01.17.
 */
public class UserAnswer extends BaseReply {
    private String username;
    private Long questionId;
    private Long userAnswerId;

    public UserAnswer(String username, Long questionId, Long userAnswerId) {
        super("UserAnswer");
        this.username = username;
        this.questionId = questionId;
        this.userAnswerId = userAnswerId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }

    public Long getUserAnswerId() {
        return userAnswerId;
    }

    public void setUserAnswerId(Long userAnswerId) {
        this.userAnswerId = userAnswerId;
    }
}
