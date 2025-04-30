package myblog.service;

import myblog.model.Post;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

import myblog.model.Post;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class PostService {
  public List<Post> findAll(){
    List<Post> posts = Arrays.asList(
            new Post(1L, "Иван", "Иванов", 30),
            new Post(2L, "Пётр", "Петров", 25),
            new Post(3L, "Мария", "Сидорова", 28)
    );
    return posts;
  }
}

