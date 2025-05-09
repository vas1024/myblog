/*
package myblog.config;

import myblog.controller.PostsController;
import myblog.repository.PostRepository;
import myblog.service.PostService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import static org.mockito.Mockito.mock;


@Configuration
public class TestConfig {


  @Bean
  public PostService postService(PostRepository postRepository) {
    return new PostService(postRepository);
  }

  @Bean
  public PostRepository postRepository() {
    // Возвращаем mock-объект вместо реального репозитория
    return mock(PostRepository.class);
  }

  @Bean
  public PostsController postsController( PostService postService ){
    return new PostsController( postService );
  }

}
*/