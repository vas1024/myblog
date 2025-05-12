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


  public List<Post> findAll(int page, int size, String search){
    List<Post> posts = repository.findAllWithComments(page,size, search);
    return posts;
  }

  public long countPosts(){ return repository.countPosts(); }


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

  public long saveNew( Post post ){
    return repository.saveNew( post );
  }

  public void saveImageById( long id, byte[] imageBytes){
    repository.saveImageById(id, imageBytes);
  }

  public void deletePostById( long id ){
    repository.deletePostById( id );
  }

  public void addComment( long id, String comment ) { repository.addComment( id, comment ); }

  public void editComment( long id, int commentId, String text ) { repository.editComment( id, commentId, text ); }

  public void deleteComment( long id, int commentId ) { repository.deleteComment( id, commentId ); }


}

