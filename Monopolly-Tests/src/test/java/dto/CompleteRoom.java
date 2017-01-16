package dto;

import java.util.ArrayList;
import java.util.List;


/**
 * CompleteRoom
 */

public class CompleteRoom   {
  private Long id = null;

  private Long owner = null;

  private String name = null;

  private List<CompletePoll> polls = new ArrayList<CompletePoll>();

  public CompleteRoom id(Long id) {
    this.id = id;
    return this;
  }
}

