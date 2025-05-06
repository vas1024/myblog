package myblog.service;


import myblog.config.TestConfigWithData;
import myblog.model.Post;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestConfigWithData.class)
public class PostServiceWithDataTest{

  @Autowired
  private PostService postService;

  @Test
  void testFindPost() {
    Post post = postService.getPostById(1L);
    assertNotNull(post);
    assertEquals("Jane Doe", post.getTitle());
  }

  @Test
  void testSavePost() {
    Post savePost = new Post(3L, "Charlie Chaplin", "some text", 0,"" );
    postService.save( savePost );

    Post foundPost = postService.getPostById(3L);
    assertNotNull(foundPost);
    assertEquals("Charlie Chaplin", foundPost.getTitle());
  }
}