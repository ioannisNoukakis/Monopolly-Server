package io.swagger.websocket;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.cristallium.api.dto.Question;
import io.swagger.DataWatcher.AnswerWatcher;
import io.swagger.database.dao.QuestionReporitory;
import io.swagger.database.dao.UserRepository;
import io.swagger.utils.JSONParser;
import io.swagger.utils.JWTutils;
import io.swagger.websocket.dto.SubscriptionMessage;
import io.swagger.websocket.dto.reply.WsResponse;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    UserRepository userRepository;

    @Autowired
    QuestionReporitory questionReporitory;

    public WebSocketController() {
        clients = Collections.<WebSocketSession>synchronizedList(new LinkedList<WebSocketSession>());
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {

        System.out.println("[WS] New Connection");
        SubscriptionMessage subscriptionMessage = (SubscriptionMessage)JSONParser.parse(message.getPayload(), SubscriptionMessage.class);

        if(!clients.contains(session) && isClientAuth(subscriptionMessage.getToken())) {
            clients.add(session);
        }else if(!isClientAuth(subscriptionMessage.getToken())){
            session.sendMessage(new TextMessage(JSONParser.toJson(new WsResponse(403))));
            return;
        }

        if(questionReporitory.findOne(subscriptionMessage.getQuestionId()) == null) {
            session.sendMessage(new TextMessage(JSONParser.toJson(new WsResponse(403))));
            return;
        }

        if(subscriptionMessage.isSubscribe())
            doSubscribe(session, subscriptionMessage.getEndpoint(), subscriptionMessage.getQuestionId());
        else
            doUnSubscribe(session, subscriptionMessage.getEndpoint(), subscriptionMessage.getQuestionId());
    }

    private boolean isClientAuth(String token)
    {
        long id;
        try{
            id = JWTutils.parseToken(token);
        }
        catch (JWTDecodeException e) {
            return false;
        }
       return userRepository.findOne(id) != null;
    }

    private void doSubscribe(WebSocketSession session, String endpoint, Long questionId) throws IOException {
        switch (endpoint)
        {
            case "answersReply":
                AnswerWatcher.getInstance().addClient(new Subscription(session, questionId));
                session.sendMessage(new TextMessage(JSONParser.toJson(new WsResponse(200))));
                break;
            default:
                session.sendMessage(new TextMessage(JSONParser.toJson(new WsResponse(404))));
        }
    }

    private void doUnSubscribe(WebSocketSession session, String endpoint, Long questionId) throws IOException {
        switch (endpoint)
        {
            case "answersReply":
                AnswerWatcher.getInstance().removeClient(new Subscription(session, questionId));
                session.sendMessage(new TextMessage(JSONParser.toJson(new WsResponse(200))));
                break;
            default:
                session.sendMessage(new TextMessage(JSONParser.toJson(new WsResponse(404))));
        }
    }
}
