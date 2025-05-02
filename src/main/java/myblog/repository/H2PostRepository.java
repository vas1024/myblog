package myblog.repository;

import myblog.model.Comment;
import myblog.model.Post;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
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


  @Override
  public void deleteById(long id) {
    jdbcTemplate.update("delete from users where id = ?", id);
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
  public List<Post> findAllWithComments() {

    List<Post> posts = jdbcTemplate.query(
            "select id, title, text, likes, tags  from posts",
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

}