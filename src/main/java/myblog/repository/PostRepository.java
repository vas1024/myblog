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
}


