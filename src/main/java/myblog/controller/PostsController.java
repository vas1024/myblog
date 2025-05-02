package myblog.controller;

import myblog.model.Paging;
import myblog.model.Post;
import myblog.service.PostService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Arrays;
import java.util.List;

import static org.springframework.http.MediaType.IMAGE_JPEG;


@Controller
public class PostsController {
  private final PostService service;
  public PostsController(PostService service) {
    this.service = service;
  }

  @GetMapping("/") // Принимаем GET-запрос по адресу /home
  public String homePage() { return "redirect:/posts"; }

  @GetMapping("/posts")
//  public String getPosts(@ModelAttribute String search,
//                         @ModelAttribute Paging paging,
//                         Model model){
  public String getPosts( Model model ) {
    System.out.println( "Hello world" );

    Paging paging = new Paging();
    paging.setPageNumber(1);
    paging.setPageSize(10);
    paging.setHasNext(false);
    paging.setHasPrevious(false);
    model.addAttribute( "paging", paging );

    List<Post> posts = service.findAll();
    model.addAttribute("posts", posts );

    for( Post e : posts ) {
      System.out.println("number of comments = " + e.getComments().size() ) ;
      System.out.println("tags = " + e.getTags() );
    }

    model.addAttribute( "search", "");

    return "posts";
  }

  @GetMapping("/images/{id}")
  public ResponseEntity<byte[]> getImage(@PathVariable( name = "id" ) Long id) {
    byte[] imageData = service.getImageById( id ); // Предполагаем, что image хранится как byte[]

    System.out.println("Тип данных: " + (imageData != null ? imageData.length : "null"));
    System.out.println("JPEG signature valid: " +
            (imageData[0] == -1 && imageData[1] == -40)); // Должно быть true

    return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_TYPE, "image/jpeg")  // Жёстко задаём тип
            .header(HttpHeaders.CONTENT_LENGTH, String.valueOf(imageData.length))
            .header(HttpHeaders.CACHE_CONTROL, "no-transform") // Запрещаем преобразования
            .body(imageData);
  }


  @GetMapping("/testimage")
  public ResponseEntity<byte[]> getTestImage() {
    byte[] testImage = service.getImage( 0L );

    return ResponseEntity.ok()
            .contentType(MediaType.IMAGE_JPEG)
            .body(testImage);
  }

  @GetMapping("/posts/{id}")
  public String getPostById(@PathVariable( name = "id" ) Long id,
                            Model model ) {

    Post post = service.getPostById( id );
    model.addAttribute("post", post );

    System.out.println("Post = " + post );

    return "post";
  }

  @PostMapping("/posts/{id}/like")
  public String changeLikes(@PathVariable( name = "id" ) long id,
                            @RequestParam( name = "like") boolean like,
                            RedirectAttributes redirectAttributes ){

    Post post = service.getPostById( id );
    int likesCount = post.getLikesCount();
    if( like ) likesCount++;
    else if( likesCount > 0 ) likesCount--;
    post.setLikesCount( likesCount );

    service.save(post);

    redirectAttributes.addFlashAttribute("updated", true);
    return "redirect:/posts/" + id;
  }


}