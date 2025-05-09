package myblog;

import org.springframework.context.annotation.*;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
//import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = {"myblog","myblog.controller","myblog.config"})
@PropertySource("classpath:application.properties") // Для считывания application.properties
public class WebConfiguration {
  /*
  @Bean
  public CommonsMultipartResolver multipartResolver() {
    CommonsMultipartResolver resolver = new CommonsMultipartResolver();
    resolver.setMaxUploadSize(10485760); // 10 MB
    return resolver;
  }
*/

  @Bean
  public StandardServletMultipartResolver multipartResolver() {
    return new StandardServletMultipartResolver();
  }

}
