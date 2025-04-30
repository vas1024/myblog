package myblog.controller;

import myblog.model.Paging;
import myblog.model.Post;
import myblog.service.PostService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Arrays;
import java.util.List;


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

    return "posts-template";
  }
}