package dto;
/**
 * Poll
 */
public class Poll   {
  private String name = null;

  public Poll(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}

