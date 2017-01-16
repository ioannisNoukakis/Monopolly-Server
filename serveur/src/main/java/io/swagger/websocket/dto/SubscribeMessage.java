package io.swagger.websocket.dto;

/**
 * Created by durza9390 on 16.01.2017.
 */
public class SubscribeMessage {
    private String token;
    private String endpoint;

    public SubscribeMessage(String token, String endpoint) {
        this.token = token;
        this.endpoint = endpoint;
    }

    public SubscribeMessage() {
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
}
