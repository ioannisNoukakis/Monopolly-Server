package dto;


import java.util.ArrayList;
import java.util.List;


/**
 * CompleteQuestion
 */
public class CompleteQuestion   {
  private Long id = null;

  private String body = null;

  private List<CompleteAsnwer> answers = new ArrayList<CompleteAsnwer>();

  public CompleteQuestion(Long id, String body, List<CompleteAsnwer> answers) {
    this.id = id;
    this.body = body;
    this.answers = answers;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getBody() {
    return body;
  }

  public void setBody(String body) {
    this.body = body;
  }

  public List<CompleteAsnwer> getAnswers() {
    return answers;
  }

  public void setAnswers(List<CompleteAsnwer> answers) {
    this.answers = answers;
  }
}

