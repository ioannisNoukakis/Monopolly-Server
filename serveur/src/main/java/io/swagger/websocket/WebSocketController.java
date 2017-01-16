package io.swagger.websocket;

import io.swagger.DataWatcher.AnswerWatcher;
import io.swagger.utils.JSONParser;
import io.swagger.websocket.dto.SubscribeMessage;
import io.swagger.websocket.dto.WsResponse;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by lux on 16.01.17.
 */
public class WebSocketController extends TextWebSocketHandler {
    private List<WebSocketSession> clients;

    public WebSocketController() {
        clients = Collections.<WebSocketSession>synchronizedList(new LinkedList<WebSocketSession>());
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        if(!clients.contains(session)) {

        }
    }

    private void doSubscribe(WebSocketSession session, String message) throws IOException {
        switch (message)
        {
            case "answersReply":
                AnswerWatcher.getInstance().addClient(session);
                session.sendMessage(new TextMessage(JSONParser.toJson(new WsResponse(200))));
                break;
            default:
                session.sendMessage(new TextMessage(JSONParser.toJson(new WsResponse(404))));
        }
    }
}
