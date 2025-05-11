package myblog.repository;

import myblog.config.DatasourceConfig;
import myblog.model.Post;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;


import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringJUnitConfig(classes = {DatasourceConfig.class, H2PostRepository.class})
@TestPropertySource(locations = "classpath:application.properties")
public class H2PostRepositoryTest {


  @Autowired
  private JdbcTemplate jdbcTemplate;

  @Autowired
  private PostRepository postRepository;

  @BeforeEach
  void setUp() {

    jdbcTemplate.execute("DELETE FROM posts");
    jdbcTemplate.execute("DELETE FROM comments");
    jdbcTemplate.execute("DELETE FROM images");
    jdbcTemplate.execute(
            "insert into posts(id, title, text, likes, tags) values ( 1, 'my very firs post', 'это самый первый блог в великой блоголенте', 3, '#bull#shit#')");
    jdbcTemplate.execute(
            "insert into posts(id, title, text, likes, tags) values ( 2, 'i will write here every day', 'а это второй пост в великой блоголенте', 2, '#bull#')");
    jdbcTemplate.execute(
            "insert into posts(id, title, text, likes, tags) values ( 3, 'its so hard to write here', 'третий текст в великой блоголенте', 100, '')") ;

  }

  @Test
  void save_shouldAddUserToDatabase() {

//    public Post(Long id, String title, String text, int likesCount, String tags)
    Post post = new Post( 100L, "add", "text", 0, "" );

    long id = postRepository.saveNew( post );

    Post savedPost = postRepository.findAll().stream()
            .filter(createdPost -> createdPost.getId().equals(id))
            .findFirst()
            .orElse(null);

    assertNotNull(savedPost);
    assertEquals("add", savedPost.getTitle());
    assertEquals("text", savedPost.getText());
  }

  @Test
  void findAll_shouldReturnAllPosts() {
    List<Post> posts = postRepository.findAll();

    assertNotNull(posts);
    assertEquals(3, posts.size());

    Post post = posts.getFirst();
    assertEquals(1L, post.getId());
    assertEquals("my very firs post", post.getTitle());
  }

  @Test
  void deletePostById_shouldRemovePostFromDatabase() {
    postRepository.deletePostById(1L);

    List<Post> posts = postRepository.findAll();

    Post deletedPost = posts.stream()
            .filter(createdPost -> createdPost.getId().equals(1L))
            .findFirst()
            .orElse(null);
    assertNull(deletedPost);
  }

  @Test
  void addComment_shouldAddCommentToDB() {
    long id = 1;
    String comment3 = "this is third comment";
    postRepository.addComment(id, "comment1");
    postRepository.addComment(id, "comment2");
    postRepository.addComment(id, comment3 );
    Post post =  postRepository.getPostById(id) ;

    assertEquals(3, post.getComments().size() );
    assertEquals( comment3, post.getComments().get(2).getText() );
  }

  @Test
  void editComment_shouldAddCommentToDB() {
    long postId = 1;
    String commentBefore = "this is new comment";
    String commentAfter = "this is comment after edit";
    postRepository.addComment(postId, commentBefore );
    Post post =  postRepository.getPostById(postId) ;
    int commentId = (int) post.getComments().get(0).getId();
    System.out.println( "Comment Id = " + commentId ); // = 4, не понимаю, почему он не 1
    postRepository.editComment(postId,commentId,commentAfter);
    post =  postRepository.getPostById(postId) ;
    assertEquals( commentAfter, post.getComments().get(0).getText() );
  }

  @Test
  void deleteComment_shouldDeleteCommentFromDB() {
    long postId = 1;
    String comment = "this is new comment";
    postRepository.addComment(postId, comment );
    Post post =  postRepository.getPostById(postId) ;
    int commentId = (int) post.getComments().get(0).getId();
    System.out.println( "Comment Id = " + commentId ); // = 4, не понимаю, почему он не 1
    postRepository.deleteComment(postId, commentId);
    post =  postRepository.getPostById(postId) ;
    assertEquals( 0, post.getComments().size() );
  }




}