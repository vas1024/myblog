package myblog.service;

import myblog.model.Comment;
import myblog.model.Post;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
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



  public static byte[] getImage( long id ) {
    String filePath = "/tmp/pachka2.jpg";

    FileInputStream fis = null;
    ByteArrayOutputStream baos = new ByteArrayOutputStream();

    try {
      fis = new FileInputStream(filePath);
      byte[] buffer = new byte[1024];
      int bytesRead;
      while ((bytesRead = fis.read(buffer)) != -1) {
        baos.write(buffer, 0, bytesRead);
      }
      return baos.toByteArray();
    } catch (IOException e) {
      e.printStackTrace();
      return null; // Возвращаем null в случае ошибки
    } finally {
      try {
        if (fis != null) {
          fis.close();
        }
        baos.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

}

