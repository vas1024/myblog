package myblog.model;

import java.util.ArrayList;
import java.util.List;

public class Post {

  private Long id;
  private String title;
  private String text;
  private String textPreview;
  private int likesCount;
  private List<Comment> comments = new ArrayList<>();
  private String tag = "";

  public Post() {}
  public Post(Long id, String title, String text, int likesCount ) {
    this.id = id;
    this.title = title;
    setText( text );
    this.likesCount = likesCount;
  }

  public void setId( Long id ) { this.id = id; }
  public void setTitle( String title ) { this.title = title; }
  public void setText(String text ) {
    this.text = text;
    this.textPreview = text.codePoints()
            .limit(3)
            .collect(StringBuilder::new,
                    (sb, cp) -> sb.appendCodePoint(cp),
                    StringBuilder::append)
            .toString();
  }
  public void setLikesCount( int likesCount ){ this.likesCount = likesCount; }
  //public void addComment( String comment ) { comments.add( comment ); }
  public void setComments( List<Comment> comments ){ this.comments = comments; }
  public void setTag( String tag ) { this.tag = tag; }

  public Long getId() { return id; }
  public String getTitle() { return title; }
  public String getText() { return text; }
  public String getTextPreview() { return textPreview; }
  public int getLikesCount() { return likesCount; }
  public List<Comment> getComments(){ return comments; }
  public String getTag() { return tag; }

}