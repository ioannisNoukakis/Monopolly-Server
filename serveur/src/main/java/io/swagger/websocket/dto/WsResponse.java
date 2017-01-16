package io.swagger.websocket.dto;

/**
 * Created by lux on 16.01.17.
 */
public class WsResponse {
    int status;

    public WsResponse() {
    }

    public WsResponse(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
