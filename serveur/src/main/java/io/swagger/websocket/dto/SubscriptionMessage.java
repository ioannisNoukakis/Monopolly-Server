package io.swagger.websocket.dto;

/**
 * Created by durza9390 on 16.01.2017.
 */
public class SubscriptionMessage {
    private String token;
    private Long subOjbectId;
    private boolean subscribe;

    public SubscriptionMessage(String token, String endpoint, Long SubOjbectId, boolean subscribe) {
        this.token = token;
        this.subOjbectId = SubOjbectId;
        this.subscribe = subscribe;
    }

    public SubscriptionMessage() {
    }

    public Long getSubOjbectId() {
        return subOjbectId;
    }

    public void setSubOjbectId(Long subOjbectId) {
        this.subOjbectId = subOjbectId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isSubscribe() {
        return subscribe;
    }

    public void setSubscribe(boolean subscribe) {
        this.subscribe = subscribe;
    }
}
