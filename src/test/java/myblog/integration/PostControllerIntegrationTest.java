package myblog.integration;

//import myblog.config.TestConfig;
import myblog.repository.H2PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

//import myblog.config.TestConfig;
import myblog.WebConfiguration;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


//@SpringJUnitConfig(classes = WebConfiguration.class)
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = WebConfiguration.class)
@WebAppConfiguration
class PostControllerIntegrationTest {

  @Autowired
  private WebApplicationContext webApplicationContext;

  @Autowired
  private JdbcTemplate jdbcTemplate;

  private MockMvc mockMvc;

  @BeforeEach
  void setUp() {
    mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

    // Очистка и заполнение тестовых данных в базе
    jdbcTemplate.execute("DELETE FROM posts");
    jdbcTemplate.execute("DELETE FROM comments");
    jdbcTemplate.execute("DELETE FROM images");
    jdbcTemplate.execute(
    "insert into posts(id, title, text, likes, tags) values ( 1, 'my very firs post', 'это самый первый блог в великой блоголенте', 3, '#bull#shit#')");
    jdbcTemplate.execute(
    "insert into posts(id, title, text, likes, tags) values ( 2, 'i will write here every day', 'а это второй пост в великой блоголенте', 2, '#bull#')");
    jdbcTemplate.execute(
    "insert into posts(id, title, text, likes, tags) values ( 3, 'its so hard to write here', 'третий текст в великой блоголенте', 100, '')") ;

  }

  @Test
  void getPosts_shouldReturnHtmlWithPosts() throws Exception {
    mockMvc.perform(get("/posts"))
            .andExpect(status().isOk())
            .andExpect(content().contentType("text/html;charset=UTF-8"))
            .andExpect(view().name("posts"))
            .andExpect(model().attributeExists("posts"))
    // xpath не может распарсить нормальный html tag <input    >
    // если усрался xpath, надо добавлять депенденси  <artifactId>nekohtml</artifactId>
//            .andExpect(xpath("//table/tr[td//h2]").nodeCount(3))
//            .andExpect(xpath("//table/tr[td[@style='border-bottom:thin solid;']]").nodeCount(3))
            ;


    String html = mockMvc.perform(get("/posts"))
            .andReturn()
            .getResponse()
            .getContentAsString();
//    System.out.println( "!!!!!!!!!!!!!!!!!!!!!!!!!!!" + html );
    assertTrue(html.contains("my very firs post"));
  }




  @Test
  void save_shouldAddPostToDatabaseAndRedirect() throws Exception {
    mockMvc.perform(post("/posts")
                    .param("title", "TITLE")
                    .param("text", "some text")
                    .param("tags", "")
            )
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/posts"));
  }


  @Test
  void delete_shouldRemovePostFromDatabaseAndRedirect() throws Exception {
    long postId = 1;
    mockMvc.perform(post("/posts/{id}/delete", postId )
                    )
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/posts"));
  }



}
