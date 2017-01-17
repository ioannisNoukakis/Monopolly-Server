package io.swagger.database.model;

import javax.persistence.*;
import java.util.List;

/**
 * Created by lux on 10.12.16.
 */
@Entity
public class User {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private long id;

    @Column(unique=true)
    private String username;
    private String password;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<CompleteRoom> rooms;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<CompleteAnswer> answers;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<CompleteRoom> subscribed;

    public User(){

    }

    public User(String username, String password, List<CompleteRoom> rooms, List<CompleteAnswer> answers, List<CompleteRoom> subscribed) {
        this.username = username;
        this.password = password;
        this.rooms = rooms;
        this.answers = answers;
        this.subscribed = subscribed;
    }

    public List<CompleteRoom> getSubscribed() {
        return subscribed;
    }

    public void setSubscribed(List<CompleteRoom> subscribed) {
        this.subscribed = subscribed;
    }

    public long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<CompleteRoom> getRooms() {
        return rooms;
    }

    public void setRooms(List<CompleteRoom> rooms) {
        this.rooms = rooms;
    }

    public List<CompleteAnswer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<CompleteAnswer> answers) {
        this.answers = answers;
    }
}
