package io.swagger.websocket;

import org.springframework.web.socket.WebSocketSession;

/**
 * Created by lux on 17.01.17.
 */
public class Subscription {
    private WebSocketSession session;
    private Long questionId;

    public Subscription(WebSocketSession session, Long questionId) {
        this.session = session;
        this.questionId = questionId;
    }

    public WebSocketSession getSession() {
        return session;
    }

    public void setSession(WebSocketSession session) {
        this.session = session;
    }

    public Long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }
}
