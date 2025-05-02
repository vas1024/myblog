package myblog.model;

public class Comment {
  private long id;
  private String text;

  public Comment( long id, String text ){
    this.id = id;
    this.text = text;
  }
  public void setId( long id ) { this.id = id; }
  public void setText( String text ) { this.text = text; }
  public long getId() { return id; }
  public String getText() { return text; }

}
