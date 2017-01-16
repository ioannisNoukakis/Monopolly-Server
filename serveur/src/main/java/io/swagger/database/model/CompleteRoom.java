package io.swagger.database.model;

import javax.persistence.*;
import java.util.List;

/**
 * Created by lux on 14.01.17.
 */
@Entity
public class CompleteRoom {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private long id;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="OWNER_ID")
    private User owner;

    private String name;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<CompletePoll> polls;

    public CompleteRoom(User owner, String name, List<CompletePoll> polls) {
        this.owner = owner;
        this.name = name;
        this.polls = polls;
    }

    public CompleteRoom() {
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPolls(List<CompletePoll> polls) {
        this.polls = polls;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<CompletePoll> getPolls() {
        return polls;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }
}
