/*
package myblog.service;


import myblog.model.Post;
import myblog.repository.PostRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestConfig.class)
public class PostServiceTest {

  @Autowired
  private PostService service;

  @Autowired
  private PostRepository postRepository;

  @Test
  void testFindUser() {
    // Настраиваем mock-объект
    Post mockPost = new Post(1L, "Jane Doe", "some text", 0,"" );
    when(postRepository.getPostById(1L)).thenReturn(mockPost);

    // Проверяем работу сервиса
    Post post = service.getPostById(1L);
    assertNotNull(post);
    assertEquals("Jane Doe", post.getTitle());
  }
}
*/

