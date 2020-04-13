package ua.edu.sumdu.j2se.holovko.tasks.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ua.edu.sumdu.j2se.holovko.tasks.config.GlobalVars;

import java.io.*;

@Controller
@RequestMapping(value = "/logs")
public class LogsController {
    @GetMapping
    public String index( Model model) throws IOException {
        InputStream is = new FileInputStream(GlobalVars.logsFilePath);
        BufferedReader buf = new BufferedReader(new InputStreamReader(is));
        String line = buf.readLine();
        StringBuilder sb = new StringBuilder();
        while(line != null){
            sb.append(line).append("<br/>");
            line = buf.readLine();
        }
        String fileAsString = sb.toString();

        model.addAttribute("fileAsString", fileAsString);
        return "logs";
    }
}
