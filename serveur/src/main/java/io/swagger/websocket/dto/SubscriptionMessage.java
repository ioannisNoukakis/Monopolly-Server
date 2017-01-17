package io.swagger.websocket.dto;

/**
 * Created by durza9390 on 16.01.2017.
 */
public class SubscriptionMessage {
    private String token;
    private String endpoint;
    private Long pollId;
    private boolean subscribe;

    public SubscriptionMessage(String token, String endpoint, Long pollId, boolean subscribe) {
        this.token = token;
        this.endpoint = endpoint;
        this.pollId = pollId;
        this.subscribe = subscribe;
    }

    public SubscriptionMessage() {
    }

    public Long getPollId() {
        return pollId;
    }

    public void setPollId(Long pollId) {
        this.pollId = pollId;
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
