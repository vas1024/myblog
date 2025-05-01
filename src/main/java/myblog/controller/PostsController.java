package myblog.controller;

import myblog.model.Paging;
import myblog.model.Post;
import myblog.service.PostService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

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
      System.out.println(e.getComments().size() ) ;
    }

    model.addAttribute( "search", "Test");

    return "posts";
  }

  @GetMapping("/images/{id}")
  public ResponseEntity<byte[]> getImage(@PathVariable( name = "id" ) Long id) {
    byte[] imageData = service.getImage( id ); // Предполагаем, что image хранится как byte[]

    System.out.println("Данные изображения: " + Arrays.toString(imageData)); // Первые 10 байт
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


}