package io.swagger.database.model;

import org.hibernate.annotations.ManyToAny;

import javax.persistence.*;
import java.util.List;

/**
 * Created by lux on 14.01.17.
 */
@Entity
public class CompleteAnswer {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private long id;

    private String body;
    private boolean isValid;

    @ManyToOne(cascade = CascadeType.ALL, fetch=FetchType.LAZY)
    @JoinColumn(name="COMPLETE_QUESTION_ID")
    private CompleteQuestion completeQuestion;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<User> user;

    public CompleteAnswer() {
    }

    public CompleteAnswer(String body, boolean isValid, CompleteQuestion completeQuestion, List<User> user) {
        this.body = body;
        this.isValid = isValid;
        this.completeQuestion = completeQuestion;
        this.user = user;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setValid(boolean valid) {
        isValid = valid;
    }

    public long getId() {
        return id;
    }

    public String getBody() {
        return body;
    }

    public boolean isValid() {
        return isValid;
    }

    public CompleteQuestion getCompleteQuestion() {
        return completeQuestion;
    }

    public void setCompleteQuestion(CompleteQuestion completeQuestion) {
        this.completeQuestion = completeQuestion;
    }

    public List<User> getUser() {
        return user;
    }

    public void setUser(List<User> user) {
        this.user = user;
    }
}
