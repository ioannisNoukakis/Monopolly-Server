package dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Question
 */
public class Question   {
  private String body = null;

  private List<Asnwer> answers = new ArrayList<Asnwer>();

  public Question(String body) {
    this.body = body;
  }

  public String getBody() {
    return body;
  }

  public void setBody(String body) {
    this.body = body;
  }

  public List<Asnwer> getAnswers() {
    return answers;
  }

  public void setAnswers(List<Asnwer> answers) {
    this.answers = answers;
  }
}

