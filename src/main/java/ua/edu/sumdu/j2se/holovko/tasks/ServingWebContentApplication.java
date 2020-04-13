package ua.edu.sumdu.j2se.holovko.tasks;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.thymeleaf.extras.java8time.dialect.Java8TimeDialect;

import java.io.*;

@SpringBootApplication
@EnableScheduling
//public class ServingWebContentApplication {
public class ServingWebContentApplication  extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(ServingWebContentApplication.class);
    }

    @Bean
    public Java8TimeDialect java8TimeDialect() {
        return new Java8TimeDialect();
    }

    public static void main(String[] args) throws ClassNotFoundException, IOException {
        SpringApplication.run(ServingWebContentApplication.class, args);
    }

}