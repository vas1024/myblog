package myblog;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = {"myblog","myblog.controller","myblog.config"})
@PropertySource("classpath:application.properties") // Для считывания application.properties
public class WebConfiguration {}
