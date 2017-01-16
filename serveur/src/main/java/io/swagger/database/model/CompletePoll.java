package io.swagger.database.model;

import javax.persistence.*;
import java.util.List;

/**
 * Created by lux on 14.01.17.
 */
@Entity
public class CompletePoll {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private long id;

    private String name;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="COMPLETE_ROOM_ID")
    private CompleteRoom completeRoom;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<CompleteQuestion> questions;

    public CompletePoll(String name, CompleteRoom completeRoom, List<CompleteQuestion> questions) {
        this.name = name;
        this.completeRoom = completeRoom;
        this.questions = questions;
    }

    public CompletePoll() {
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

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setQuestions(List<CompleteQuestion> questions) {
        this.questions = questions;
    }

    public CompleteRoom getCompleteRoom() {
        return completeRoom;
    }

    public void setCompleteRoom(CompleteRoom completeRoom) {
        this.completeRoom = completeRoom;
    }
}
