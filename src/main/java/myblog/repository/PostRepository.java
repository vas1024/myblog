package myblog.repository;

import myblog.model.Post;

import java.util.List;

public interface PostRepository {
  List<Post> findAll();
  void save(Post post);
  void deleteById(long id);
  byte[] getImage(long id);
}


