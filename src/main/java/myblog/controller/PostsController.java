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


  @GetMapping("/")
  public String homePage() { return "redirect:/posts"; }


  @GetMapping("/posts")
  public String getPosts(
                           @RequestParam(name = "search", required = false, defaultValue = "" ) String search,
                           @RequestParam(name = "pageSize", defaultValue = "10") int size,
                           @RequestParam(name = "pageNumber", defaultValue = "1") int page,
                           Model model
                        ) {
    long total = postService.countPosts();
    boolean hasNext = page * size < total;
    boolean hasPrevious = page > 1;
    Paging paging = new Paging();
    paging.setPageNumber( page );
    paging.setPageSize( size );
    paging.setHasNext( hasNext );
    paging.setHasPrevious( hasPrevious );
    model.addAttribute("paging", paging);

    model.addAttribute("search", search );

    List<Post> posts = postService.findAll(page, size, search );
    model.addAttribute("posts", posts );

    return "posts";
  }


  @GetMapping("/images/{id}")
  public ResponseEntity<byte[]> getImage(@PathVariable( name = "id" ) Long id) {
    byte[] imageData = postService.getImageById( id ); // Предполагаем, что image хранится как byte[]


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


  @PostMapping("posts/{id}/comments")
  public String addComment(
              @PathVariable("id") Long id,
              @RequestParam(value = "text", required = false) String comment
            ) {
    postService.addComment( id, comment );
    return "redirect:/posts/" + id;
  }

  @PostMapping("posts/{id}/comments/{commentId}")
  public String editComment(
          @PathVariable("id") Long id,
          @RequestParam(value = "text", required = false) String text,
          @PathVariable("commentId") int commentId
        ) {
    postService.editComment( id, commentId, text);
    return "redirect:/posts/" + id;
  }

  @PostMapping("posts/{id}/comments/{commentId}/delete")
  public String deleteComment(
          @PathVariable("id") Long id,
          @PathVariable("commentId") int commentId
  ) {
    postService.deleteComment( id, commentId );
    return "redirect:/posts/" + id;
  }

}