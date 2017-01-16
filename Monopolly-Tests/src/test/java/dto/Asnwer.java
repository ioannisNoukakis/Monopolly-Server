package dto;

/**
 * Asnwer
 */
public class Asnwer   {
  private String body = null;
  private Boolean isValid = null;

  public Asnwer(String body, Boolean isValid) {
    this.body = body;
    this.isValid = isValid;
  }

  public String getBody() {
    return body;
  }

  public void setBody(String body) {
    this.body = body;
  }

  public Boolean getValid() {
    return isValid;
  }

  public void setValid(Boolean valid) {
    isValid = valid;
  }
}

