package io.swagger.DataWatcher;

import io.swagger.websocket.Subscription;
import org.springframework.web.socket.TextMessage;

import java.io.IOException;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by durza9390 on 18.01.2017.
 */
public class QuestionWatcher {
    private static QuestionWatcher instance;
    private List<Subscription> clients;

    public static QuestionWatcher getInstance() {
        if(instance == null)
            instance = new QuestionWatcher();
        return instance;
    }

    private QuestionWatcher() {
        clients = Collections.<Subscription>synchronizedList(new LinkedList<Subscription>());
    }

    public void addClient(Subscription subscription)
    {
        clients.add(subscription);
    }

    public void removeClient(Subscription subscription)
    {
        clients.remove(subscription);
    }

    public void notifyClients(String message, Long suObjectId) {
        Iterator<Subscription> iterator = clients.iterator();

        System.out.println("[QUESTION_WATCHER] Notifying clients on " + suObjectId + "...");
        synchronized (clients) {
            while (iterator.hasNext()) {
                Subscription su = iterator.next();
                try {
                    if(su.getSubOjbectId() == suObjectId) {
                        System.out.println("[ANSWER_WATCHER] Message sent.");
                        su.getSession().sendMessage(new TextMessage(message));
                    }
                } catch (IOException e) {
                    clients.remove(su);
                }
            }
        }
    }
}
