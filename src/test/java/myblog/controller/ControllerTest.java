package myblog.controller;

import myblog.model.Post;
import myblog.service.PostService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PostsController.class)
public class ControllerTest {
  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private PostService userService;

  @Test
  void testGetAllUsers() throws Exception {

    Post post = new Post( 100L, "add", "text", 0, "" );

    when(userService.findAll(1,10,"")).thenReturn(List.of(post));

    mockMvc.perform(get("/posts"))
            .andExpect(status().isOk())
            .andExpect(content().contentType("text/html;charset=UTF-8"))
            .andExpect(view().name("posts"))
            .andExpect(model().attributeExists("posts"));

    verify(userService, times(1)).findAll(1,10,"");

  }
}
