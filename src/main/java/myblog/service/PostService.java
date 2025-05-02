package myblog.service;

import myblog.model.Comment;
import myblog.model.Post;
import myblog.repository.PostRepository;
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
  private final PostRepository repository;
  public PostService( PostRepository repository ){ this.repository = repository; }

  public List<Post> findAll(){

    List<Post> posts = repository.findAllWithComments();

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

  public byte[] getImageById( long id ) {
    return repository.getImageById( id );
  }

  public Post getPostById( long id ){
    return repository.getPostById(id);
  }

  public void save( Post post ){
    repository.save( post );
  }
}

