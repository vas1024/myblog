package myblog.model;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


public class Post {

//  private final TextPreviewSize textPreviewSize ;

  private final int textPreviewSize = 5;

  private Long id;
  private String title;
  private String text;
  private String textPreview;
  private int likesCount;
  private List<Comment> comments = new ArrayList<>();
  private List<String> tags;
  private String tagsString;

  public Post() {}  // Обязательно! для тимьяна

  public Post(Long id, String title, String text, int likesCount, String tags) {
    this.id = id;
    this.title = title;
    setText(text);
    this.likesCount = likesCount;
    setTags(tags);
  }

  public void setId(Long id) { this.id = id; }

  public void setTitle(String title) { this.title = title; }

  public void setText(String text) {
    this.text = text;
    this.textPreview = text.codePoints()
            .limit(textPreviewSize)
            .collect(StringBuilder::new,
                    (sb, cp) -> sb.appendCodePoint(cp),
                    StringBuilder::append)
            .toString();
  }

  public void setLikesCount(int likesCount) { this.likesCount = likesCount; }

  //public void addComment( String comment ) { comments.add( comment ); }
  public void setComments(List<Comment> comments) {
    this.comments = comments;
  }

  public void addComment(Comment comment) { comments.add(comment); }

  public void setTags(String tagsString) {
    this.tagsString = tagsString;
    tags = Arrays.stream(tagsString.split("#"))
            .filter(tag -> !tag.isEmpty())
            .collect(Collectors.toList());
  }

  public void setTagsAsText( String tags ){ this.tagsString = tags; }


  public Long getId() { return id; }

  public String getTitle() { return title; }

  public String getText() { return text; }

  public String getTextPreview() { return textPreview;  }

  public int getLikesCount() { return likesCount; }

  public List<Comment> getComments() { return comments; }

  public List<String> getTags() { return tags; }

  public String getTagsString(){ return tagsString; }

  public String getTagsAsText(){ return tagsString; }

  public List<String> getTextParts() {
    return Arrays.asList(text.split("\\r?\\n"));
  }


  @Override
  public String toString() {
    return "Post{" +
            "id=" + id +
            ", title='" + title + '\'' +
            ", text='" + text + '\'' +
            ", tags=" + tags + '\'' +
            ",   tagsString = " + tagsString +
            ",   likesCount = " + likesCount +
            '}';
  }


}