package ua.edu.sumdu.j2se.holovko.tasks.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import java.io.IOException;

@Controller
public class DefaultController {
    @RequestMapping(value = "/")
    public String index() throws IOException {
        return "index";
    }

}
