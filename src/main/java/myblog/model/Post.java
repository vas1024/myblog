package myblog.model;

public class Post {

  private Long id;
  private String title;
  private String text;
  private String textPreview;
  private int likesCount;


  // Конструктор без аргументов
  public Post() {}

  // Конструктор с аргументами для удобства использования
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

  public Long getId() { return id; }
  public String getTitle() { return title; }
  public String getText() { return text; }
  public String getTextPreview() { return textPreview; }
  public int getLikesCount() { return likesCount; }


}