package io.swagger.database.model;

import javax.persistence.*;
import java.util.List;

/**
 * Created by lux on 14.01.17.
 */
@Entity
public class CompleteQuestion {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private long id;

    private String body;

    private boolean closed;

    @ManyToOne(cascade = CascadeType.ALL, fetch=FetchType.LAZY)
    @JoinColumn(name="COMPLETE_POLL_ID")
    private CompleteRoom completeRoom;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<CompleteAnswer> answers;

    public CompleteQuestion(String body, boolean closed, CompleteRoom completeRoom, List<CompleteAnswer> answers) {
        this.body = body;
        this.closed = closed;
        this.completeRoom = completeRoom;
        this.answers = answers;
    }

    public CompleteQuestion() {
    }

    public long getId() {
        return id;
    }

    public String getBody() {
        return body;
    }

    public List<CompleteAnswer> getAnswers() {
        return answers;
    }

    public void setId(long id) {
        this.id = id;

    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setAnswers(List<CompleteAnswer> answers) {
        this.answers = answers;
    }

    public CompleteRoom getCompleteRoom() {
        return completeRoom;
    }

    public void setCompleteRoom(CompleteRoom completeRoom) {
        this.completeRoom = completeRoom;
    }

    public boolean isClosed() {
        return closed;
    }

    public void setClosed(boolean closed) {
        this.closed = closed;
    }
}
