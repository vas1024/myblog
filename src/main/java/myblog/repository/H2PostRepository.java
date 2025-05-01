package myblog.repository;

import myblog.model.Post;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.util.List;

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
  public void save(Post post) {
    /*
      jdbcTemplate.update("insert into users(first_name, last_name, age, active) values(?, ?, ?, ?)",
            user.getFirstName(), user.getLastName(), user.getAge(), user.isActive());
     */
  }

  @Override
  public void deleteById(long id) {
    jdbcTemplate.update("delete from users where id = ?", id);
  }

  @Override
  public byte[] getImage( long id ){
    byte[] arr = new byte[1];
    return arr;
  }

}