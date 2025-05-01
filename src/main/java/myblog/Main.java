package myblog;


import myblog.model.Post;

import java.util.Arrays;
import java.util.List;

public class Main {
  public static void main(String[] args) {

    List<Post> posts = Arrays.asList(
            new Post(1L, "Иван", "Иванов", 30, "bull"),
            new Post(2L, "Пётр", "Петров", 25, ""),
            new Post(3L, "Мария", "Сидорова", 28, "")
    );

    for( Post e : posts ) {
      System.out.println( e.getTextPreview() );
    }
  }
}