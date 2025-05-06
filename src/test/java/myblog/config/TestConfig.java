package myblog.config;

import myblog.repository.PostRepository;
import myblog.service.PostService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import static org.mockito.Mockito.mock;

@Configuration
public class TestConfig {

  @Bean
  public PostService service(PostRepository postRepository) {
    return new PostService(postRepository);
  }

  @Bean
  public PostRepository postRepository() {
    // Возвращаем mock-объект вместо реального репозитория
    return mock(PostRepository.class);
  }
}