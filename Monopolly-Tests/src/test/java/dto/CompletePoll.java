package dto;

import java.util.ArrayList;
import java.util.List;

/**
 * CompletePoll
 */

public class CompletePoll   {
  private Long id = null;

  private String name = null;

  private List<CompleteQuestion> questions = new ArrayList<CompleteQuestion>();

    public CompletePoll(Long id, String name, List<CompleteQuestion> questions) {
        this.id = id;
        this.name = name;
        this.questions = questions;
    }

    public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public List<CompleteQuestion> getQuestions() {
    return questions;
  }

  public void setQuestions(List<CompleteQuestion> questions) {
    this.questions = questions;
  }
}

