package myblog.repository;

import myblog.model.Comment;
import myblog.model.Post;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@Repository
public class H2PostRepository implements PostRepository {

  private final JdbcTemplate jdbcTemplate;

  public H2PostRepository(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  @Override
  public List<Post> findAll() {
    // Выполняем запрос с помощью JdbcTemplate
    // Преобразовываем ответ с помощью RowMapper
    return jdbcTemplate.query(
            "select id, title, text, likes, tags  from posts",
            (rs, rowNum) -> new Post(
                    rs.getLong("id"),
                    rs.getString("title"),
                    rs.getString("text"),
                    rs.getInt("likes"),
                    rs.getString("tags")
            ));
  }



  public List<Comment> getCommentsById(long id ){
    String sql = "select id, text from comments where postid = ?";
    return jdbcTemplate.query( sql,
            ( rs, rownum ) -> new Comment(
                    rs.getLong("id"),
                    rs.getString("text")
                    ), id);
  }

  @Override
  public List<Post> findAllWithComments( int page, int size) {
    int offset = ( page -1 ) * size;
    List<Post> posts = jdbcTemplate.query(
            "select id, title, text, likes, tags  from posts ORDER BY id LIMIT ? OFFSET ?",
            new Object[]{size, offset},
            (rs, rowNum) -> new Post(
                    rs.getLong("id"),
                    rs.getString("title"),
                    rs.getString("text"),
                    rs.getInt("likes"),
                    rs.getString("tags")
            ));

    //список posts и postMap ссылаются на одни и те же объекты Post, поэтому изменения в одном отразятся в другом.
    Map<Long, Post> postMap = new HashMap<>();
    posts.forEach(post -> postMap.put(post.getId(), post));

    jdbcTemplate.query(
            "SELECT id, text, postid FROM comments",
            (rs, rowNum) -> {
              Comment comment = new Comment(
                      rs.getLong("id"),
                      rs.getString("text")
              );
              // Находим пост по post_id и добавляем комментарий
              long postId = rs.getLong("postid");
              Post post = postMap.get( postId );
              if (post != null) {
                post.addComment( comment );
              }
              return null;  // Результат не нужен, мы уже добавили комментарий
            }
    );

    return posts;
  }

  @Override
  public long countPosts() {
    return jdbcTemplate.queryForObject(
            "SELECT COUNT(*) FROM posts", Long.class);
  }

  @Override
  public byte[] getImageById( long id ){
    return jdbcTemplate.queryForObject(
            "select image from images where postid = ?",
            new Object[]{id},
            (rs, rowNum) -> rs.getBytes("image")
    );
  }

  @Override
  public Post getPostById( long id ){

    List<Post> posts = jdbcTemplate.query(
            "select id, title, text, likes, tags  from posts where id = ?",
            (rs, rowNum) -> new Post(
                    rs.getLong("id"),
                    rs.getString("title"),
                    rs.getString("text"),
                    rs.getInt("likes"),
                    rs.getString("tags")
            ),
            id );
    if( posts.isEmpty() ) return new Post() ;

    List<Comment> comments = jdbcTemplate.query(
            "select id, text from comments where postid = ?",
            ( rs, rowNum ) -> new Comment(
                    rs.getLong( "id"),
                    rs.getString("text")
            ),
            id );

    posts.get(0).setComments(comments);
    return posts.get(0);
  }

  @Override
  public void save( Post post ){
    jdbcTemplate.update("UPDATE posts SET title = ?, text = ?, likes = ?, tags = ? WHERE id = ?",
            post.getTitle(), post.getText(), post.getLikesCount(), post.getTagsString(), post.getId() );
  }

  @Override
  public void saveImageById( long id, byte[] imageBytes){
    // Возвращает количество удаленных строк (0 если записи не было)
    int rows = jdbcTemplate.update("delete from images where postid = ?", id);
    System.out.println("from table Images were removerd " + rows + " records with postid " + id);
    jdbcTemplate.update( "insert into images(postid, image) values ( ?, ? )",
            id, imageBytes );
  }

  @Override
  public long saveNew(Post post){
    KeyHolder keyHolder = new GeneratedKeyHolder();

    jdbcTemplate.update(connection -> {
      PreparedStatement ps = connection.prepareStatement(
             "insert into posts( title, text, likes, tags ) values (  ?, ?, ?, ? )",
              Statement.RETURN_GENERATED_KEYS
      );
      ps.setString(1, post.getTitle() );
      ps.setString(2, post.getText() );
      ps.setInt(   3, post.getLikesCount() );
      ps.setString(4, post.getTagsString() );
      return ps;
    }, keyHolder);

    return keyHolder.getKey().longValue();
  }

  @Override
  public void deletePostById( long id ){
    jdbcTemplate.update("DELETE FROM posts WHERE id = ?", id);
    //Каскадное удаление уже определено в DDL:
    //FOREIGN KEY (postid) REFERENCES posts(id) ON DELETE CASCADE
    //Эта строка означает, что при удалении записи из posts
    // связанные записи из comments и images будут удалены автоматически самой СУБД.
  }

  @Override
  public void addComment( long id, String comment ){
    jdbcTemplate.update( "insert into comments(postid, text) values ( ?, ? )",
            id, comment );
  }

  @Override
  public void editComment( long id, int commentId, String text ){
    jdbcTemplate.update( "update comments set text = ? where postid = ? and id = ?",
            text, id, commentId );
  }

  @Override
  public void deleteComment( long id, int commentId){
    jdbcTemplate.update( "delete from comments where postid = ? and id = ?",
             id, commentId );
  }

  @Override
  public List<Post> findAll(int page, int size) {
    int offset = page * size;
    return jdbcTemplate.query(
            "select id, title, text, likes, tags  from posts LIMIT ? OFFSET ?",
            (rs, rowNum) -> new Post(
                    rs.getLong("id"),
                    rs.getString("title"),
                    rs.getString("text"),
                    rs.getInt("likes"),
                    rs.getString("tags")
            ));
  }

}