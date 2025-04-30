package myblog.service;

import myblog.model.Comment;
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
    List<Comment> comments = Arrays.asList(
            new Comment( 1, "first comment" ),
            new Comment( 2, "secont comment"),
            new Comment( 3, "third comment")
    );

    posts.get(0).setComments( comments );
    System.out.println( "size of comments of first post = " + posts.get(0).getComments().size());
    return posts;
  }
}

