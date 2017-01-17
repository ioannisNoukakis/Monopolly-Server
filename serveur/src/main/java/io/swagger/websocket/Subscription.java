package io.swagger.websocket;

import org.springframework.web.socket.WebSocketSession;

/**
 * Created by lux on 17.01.17.
 */
public class Subscription {
    private WebSocketSession session;
    private Long poillid;

    public Subscription(WebSocketSession session, Long poillid) {
        this.session = session;
        this.poillid = poillid;
    }

    public WebSocketSession getSession() {
        return session;
    }

    public void setSession(WebSocketSession session) {
        this.session = session;
    }

    public Long getPoillid() {
        return poillid;
    }

    public void setPoillid(Long poillid) {
        this.poillid = poillid;
    }
}
