package io.swagger.websocket.dto.reply;

/**
 * Created by lux on 16.01.17.
 */
public class WsResponse extends BaseReply{
    int status;

    public WsResponse() {
        super("wsResponse");
    }

    public WsResponse(int status) {
        super("wsResponse");
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
