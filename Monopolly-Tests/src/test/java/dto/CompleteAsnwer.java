package dto;

/**
 * CompleteAsnwer
 */

public class CompleteAsnwer   {
  private Long id = null;

  private String body = null;

  private Boolean isValid = null;

  public CompleteAsnwer(Long id, String body, Boolean isValid) {
    this.id = id;
    this.body = body;
    this.isValid = isValid;
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

  public Boolean getValid() {
    return isValid;
  }

  public void setValid(Boolean valid) {
    isValid = valid;
  }
}

