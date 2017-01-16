package io.swagger.DataWatcher;

import com.cristallium.api.dto.CompleteQuestion;
import com.cristallium.api.dto.Question;
import io.swagger.utils.JSONParser;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by durza9390 on 16.01.2017.
 */
public class AnswerWatcher {

    private static AnswerWatcher instance;
    private List<WebSocketSession> clients;

    public static AnswerWatcher getInstance() {
        if(instance == null)
            instance = new AnswerWatcher();
        return instance;
    }

    private AnswerWatcher() {
        clients = Collections.<WebSocketSession>synchronizedList(new LinkedList<WebSocketSession>());
    }

    public void addClient(WebSocketSession session)
    {
        clients.add(session);
    }

    public void removeClient(WebSocketSession session)
    {
        clients.remove(session);
    }

    public void notifyClients(String message) throws IOException {
        Iterator<WebSocketSession> iterator = clients.iterator();

        synchronized (clients) {
            while (iterator.hasNext()) {
                WebSocketSession next = iterator.next();
                next.sendMessage(new TextMessage(message));
            }
        }
    }
}
