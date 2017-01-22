package io.swagger.websocket;

import com.auth0.jwt.exceptions.JWTDecodeException;
import io.swagger.DataWatcher.DataWatcher;
import io.swagger.database.dao.QuestionReporitory;
import io.swagger.database.dao.RoomRepository;
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

    @Autowired
    UserRepository userRepository;

    @Autowired
    QuestionReporitory questionReporitory;

    @Autowired
    RoomRepository roomRepository;

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {

        System.out.println("[WS] New Connection");
        if(message.getPayload().equals("hello"))
        {
            System.out.println("hello!");
            session.sendMessage(new TextMessage(JSONParser.toJson(new WsResponse(200))));
            return;
        }
        SubscriptionMessage subscriptionMessage = (SubscriptionMessage)JSONParser.parse(message.getPayload(), SubscriptionMessage.class);

         if(!isClientAuth(subscriptionMessage.getToken())){
            session.sendMessage(new TextMessage(JSONParser.toJson(new WsResponse(403))));
            return;
        }

        if(questionReporitory.findOne(subscriptionMessage.getSubOjbectId()) == null && roomRepository.findOne(subscriptionMessage.getSubOjbectId()) == null) {
            session.sendMessage(new TextMessage(JSONParser.toJson(new WsResponse(404))));
            return;
        }

        if(subscriptionMessage.isSubscribe())
            doSubscribe(session, subscriptionMessage.getSubOjbectId());
        else
            doUnSubscribe(session, subscriptionMessage.getSubOjbectId());
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

    private void doSubscribe(WebSocketSession session, Long roomId) throws IOException {
        DataWatcher.getInstance().addClient(new Subscription(session, roomId));
        session.sendMessage(new TextMessage(JSONParser.toJson(new WsResponse(200))));
        System.out.println("SUCCESSFULLY SUSCRIBED TO " + roomId);
    }

    private void doUnSubscribe(WebSocketSession session, Long roomId) throws IOException {
        DataWatcher.getInstance().removeClient(new Subscription(session, roomId));
        session.sendMessage(new TextMessage(JSONParser.toJson(new WsResponse(200))));
    }
}
