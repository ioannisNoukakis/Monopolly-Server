package io.swagger.DataWatcher;

import io.swagger.websocket.Subscription;
import org.springframework.web.socket.TextMessage;

import java.io.IOException;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by durza9390 on 16.01.2017.
 */
public class DataWatcher {

    private static DataWatcher instance;
    private List<Subscription> clients;

    public static DataWatcher getInstance() {
        if(instance == null)
            instance = new DataWatcher();
        return instance;
    }

    private DataWatcher() {
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

    public synchronized void notifyClients(String message, Long questionId) {
        Iterator<Subscription> iterator = clients.iterator();

        System.out.println("[ANSWER_WATCHER] Notifying clients on " + questionId + "...");
            while (iterator.hasNext()) {
                Subscription su = iterator.next();
                try {
                    if(su.getSubOjbectId() == questionId) {
                        System.out.println("[ANSWER_WATCHER] Message sent.");
                        su.getSession().sendMessage(new TextMessage(message));
                    }
                } catch (IOException | IllegalStateException e) {
                    clients.remove(su);
                }
        }
    }
}
