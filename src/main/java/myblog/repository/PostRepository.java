package myblog.repository;

import myblog.model.Post;

import java.util.List;

public interface PostRepository {
  List<Post> findAll();
  List<Post> findAll(int page, int size);
  byte[] getImageById(long id);
  List<Post> findAllWithComments(int page, int size) ;
  long countPosts();
  Post getPostById( long id );
  void save(Post post);
  long saveNew(Post post);
  void saveImageById( long id, byte[] imageBytes);
  void deletePostById( long id );
  void addComment( long id, String comment );
  void editComment( long id, int commentId, String text );
  void deleteComment( long id, int commentId );
}


