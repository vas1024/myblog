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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
//import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.IMAGE_JPEG;


@Controller
public class PostsController {
  private final PostService postService;
  public PostsController(PostService postService) {
    this.postService = postService;
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

    List<Post> posts = postService.findAll();
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
    byte[] imageData = postService.getImageById( id ); // Предполагаем, что image хранится как byte[]

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
    byte[] testImage = postService.getImage( 0L );

    return ResponseEntity.ok()
            .contentType(MediaType.IMAGE_JPEG)
            .body(testImage);
  }

  @GetMapping("/posts/{id}")
  public String getPostById(@PathVariable( name = "id" ) Long id,
                            Model model,
                            @RequestHeader(value = "Referer", required = false) String referer ) {

    Post post = postService.getPostById( id );
    model.addAttribute("post", post );

    System.out.println("Post = " + post );
    System.out.println("referrer = " + referer );

    return "post";
  }

  @PostMapping("/posts/{id}/like")
  public String changeLikes(@PathVariable( name = "id" ) long id,
                            @RequestParam( name = "like") boolean like,
                            RedirectAttributes redirectAttributes ){

    Post post = postService.getPostById( id );
    int likesCount = post.getLikesCount();
    if( like ) likesCount++;
    else if( likesCount > 0 ) likesCount--;
    post.setLikesCount( likesCount );

    postService.save(post);

    redirectAttributes.addFlashAttribute("updated", true);
    return "redirect:/posts/" + id;
  }


  @GetMapping("/posts/{id}/edit")
  public String editPost( @PathVariable( name = "id" ) long id,
                          Model model,
                          RedirectAttributes redirectAttributes ){
    Post post = postService.getPostById( id );
    model.addAttribute("post", post );

    System.out.println( "Controller, method editPost got post object: ");
    System.out.println( post );

    return "add-post";
  }

  @GetMapping("/posts/add")
  public String addPost( Model model ){

    return "add-post";
  }

  @PostMapping("/posts/{id}")
  public String savePostById(
          @PathVariable("id") Long id,
          @RequestParam(value = "image", required = false) MultipartFile file,
          @ModelAttribute(name = "post") Post post      ) {

    saveImage( id, file );
    postService.save( post );
    return "redirect:/posts/" + id ;
  }

  @PostMapping("/posts")
  public String saveNewPost(
          @RequestParam(value = "image", required = false) MultipartFile file,
          @ModelAttribute(name = "post") Post post      ) {

    long id = postService.saveNew( post );
    saveImage( id, file );
    return "redirect:/posts";
  }


  public void saveImage( long id, MultipartFile file ){
    System.out.println("File: " + (file != null ? file.getOriginalFilename() : "null"));
    if( file != null ) {
      byte[] imageBytes;
      try {
        imageBytes = file.getBytes();
      } catch (IOException e) {
        throw new RuntimeException("Ошибка чтения файла", e);
      }
      postService.saveImageById(id, imageBytes);
    }
  }

  @PostMapping("/posts/{id}/delete")
  public String deletePost( @PathVariable(name="id") long id){
    postService.deletePostById( id );
    return "redirect:/posts";
  }



}