package myblog.model;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;

import java.util.ArrayList;
import java.util.List;

public class Post {

//  private final TextPreviewSize textPreviewSize ;

  private final int textPreviewSize = 5 ;

  private Long id;
  private String title;
  private String text;
  private String textPreview;
  private int likesCount;
  private List<Comment> comments = new ArrayList<>();
  private String tags = "";

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
            .limit( textPreviewSize )
            .collect(StringBuilder::new,
                    (sb, cp) -> sb.appendCodePoint(cp),
                    StringBuilder::append)
            .toString();

    System.out.println( "texPreviewSize = " + textPreviewSize );

  }
  public void setLikesCount( int likesCount ){ this.likesCount = likesCount; }
  //public void addComment( String comment ) { comments.add( comment ); }
  public void setComments( List<Comment> comments ){ this.comments = comments; }
  public void setTags( String tags ) { this.tags = tags; }

  public Long getId() { return id; }
  public String getTitle() { return title; }
  public String getText() { return text; }
  public String getTextPreview() { return textPreview; }
  public int getLikesCount() { return likesCount; }
  public List<Comment> getComments(){ return comments; }
  public String getTags() { return tags; }

}