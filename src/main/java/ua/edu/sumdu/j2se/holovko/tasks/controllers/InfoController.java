package ua.edu.sumdu.j2se.holovko.tasks.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(value = "/info")
public class InfoController {
    @GetMapping
    public String index( Model model){
        return "info";
    }
}
