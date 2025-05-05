package myblog.repository;

import myblog.model.Post;

import java.util.List;

public interface PostRepository {
  List<Post> findAll();
  void deleteById(long id);
  byte[] getImageById(long id);
  List<Post> findAllWithComments() ;
  Post getPostById( long id );
  void save(Post post);
  long saveNew(Post post);
  void saveImageById( long id, byte[] imageBytes);
  void deletePostById( long id );
}


