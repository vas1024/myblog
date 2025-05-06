package myblog.config;


import myblog.model.Post;
import myblog.repository.PostRepository;
import myblog.service.PostService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class TestConfigWithData {

  @Bean
  public PostService service(PostRepository postRepository) {
    return new PostService(postRepository);
  }

  @Bean
  public PostRepository postRepository() {
    return new InMemoryUserRepository(); // Тестовый репозиторий
  }

  static abstract class InMemoryUserRepository implements PostRepository {
    private final Map<Long, Post> posts = new HashMap<>();

    public InMemoryUserRepository() {
      posts.put(1L, new Post(1L, "Jane Doe", "some text", 0,"" ) );
      posts.put(2L, new Post(2L, "Jim Beam", "some text", 0,"" ) );
    }

    @Override
    public Post getPostById(Long id) {
      return posts.get(id);
    }

    @Override
    public void save(Post post) {
      posts.put(post.getId(), post);
    }
  }
}