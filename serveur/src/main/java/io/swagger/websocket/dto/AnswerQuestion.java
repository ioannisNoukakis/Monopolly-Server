package io.swagger.websocket.dto;

/**
 * Created by lux on 16.01.17.
 */
public class AnswerQuestion {
    long answerId;
    long userId;

    public AnswerQuestion() {
    }

    public long getAnswerId() {
        return answerId;
    }

    public void setAnswerId(long answerId) {
        this.answerId = answerId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }
}
