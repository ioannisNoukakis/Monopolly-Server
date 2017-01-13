package io.swagger.database.model;

import javax.persistence.*;

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

    public User(){

    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
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
}
