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
        if (!clients.contains(subscription))
            clients.add(subscription);
    }

    public void removeClient(Subscription subscription)
    {
        clients.remove(subscription);
    }

    public synchronized void notifyClients(String message, Long roomId) {
        Iterator<Subscription> iterator = clients.iterator();

        System.out.println("[ANSWER_WATCHER] Notifying clients on " + roomId + "...");
        for(int i = 0; i < clients.size(); i++) {
            try {
                if(clients.get(i).getSubOjbectId() == roomId) {

                    clients.get(i).getSession().sendMessage(new TextMessage(message));
                    System.out.println("[ANSWER_WATCHER] Message sent.");
                }
            } catch (IOException | IllegalStateException e) {
                clients.remove(clients.get(i));
                i--;
                System.out.println("[ANSWER_WATCHER] removed client " + i);
            }
        }
    }
}
