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

    @Column(unique=true)
    private String name;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<CompleteQuestion> questions;

    public CompleteRoom(User owner, String name, List<CompleteQuestion> questions) {
        this.owner = owner;
        this.name = name;
        this.questions = questions;
    }

    public CompleteRoom() {
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setQuestions(List<CompleteQuestion> questions) {
        this.questions = questions;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<CompleteQuestion> getQuestions() {
        return questions;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }
}
