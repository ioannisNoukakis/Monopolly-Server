package io.swagger.websocket.dto;

/**
 * Created by durza9390 on 16.01.2017.
 */
public class SubscriptionMessage {
    private String token;
    private String endpoint;
    private Long questionId;
    private boolean subscribe;

    public SubscriptionMessage(String token, String endpoint, Long questionId, boolean subscribe) {
        this.token = token;
        this.endpoint = endpoint;
        this.questionId = questionId;
        this.subscribe = subscribe;
    }

    public SubscriptionMessage() {
    }

    public Long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public boolean isSubscribe() {
        return subscribe;
    }

    public void setSubscribe(boolean subscribe) {
        this.subscribe = subscribe;
    }
}
