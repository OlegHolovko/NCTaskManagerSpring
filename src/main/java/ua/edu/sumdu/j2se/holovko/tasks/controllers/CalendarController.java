package ua.edu.sumdu.j2se.holovko.tasks.controllers;

import com.google.gson.Gson;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ua.edu.sumdu.j2se.holovko.tasks.config.GlobalVars;
import ua.edu.sumdu.j2se.holovko.tasks.models.*;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import static java.time.temporal.TemporalAdjusters.*;
import java.time.format.DateTimeFormatter;

@Controller
public class CalendarController {
    @RequestMapping(value = {"/calendar","/calendar/{start}/{end}"}, method = { RequestMethod.GET })
    public String index(@PathVariable(value="start", required = false) String startTime,
                        @PathVariable(value="end", required = false) String endTime,
                        Model model) throws IOException {
        Gson gson = new Gson();
        String events = null;
        LocalDateTime initial = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        LocalDateTime startTimeFormatted, endTimeFormatted;
        if(startTime!= null) {
            startTimeFormatted = LocalDateTime.parse(startTime, formatter);
        } else {
            startTimeFormatted = initial.with(firstDayOfMonth());
        }
        if(endTime!= null) {
            endTimeFormatted = LocalDateTime.parse(endTime, formatter);
        } else {
            endTimeFormatted = initial.with(lastDayOfMonth());
        }
        AbstractTaskList taskList = new ArrayTaskList();
        File file = new File(GlobalVars.dataFilePath);
        if (file.exists()) {
            TaskIO.readBinary(taskList, file);
            events = gson.toJson(Tasks.fullcalendar(taskList, startTimeFormatted, endTimeFormatted));
        }

        model.addAttribute("pickerStart", startTime);
        model.addAttribute("pickerEnd", endTime);
        model.addAttribute("events", events);

        return "calendar";
    }
}
