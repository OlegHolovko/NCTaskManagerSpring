package ua.edu.sumdu.j2se.holovko.tasks.controllers;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import ua.edu.sumdu.j2se.holovko.tasks.models.*;
import ua.edu.sumdu.j2se.holovko.tasks.models.storage.FileStore;
import javax.validation.Valid;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping(value = "/task")
public class TaskController {

    private static final Logger logger = LogManager.getLogger(TaskController.class);

    @GetMapping
    public String index(Model model) throws IOException {
        model.addAttribute("tasks", FileStore.readList());
        return "list";
    }

    @RequestMapping(value = "/list", method = { RequestMethod.GET})
    public String list( Model model) throws IOException {
        model.addAttribute("tasks", FileStore.readList());
        return "list";
    }

    @RequestMapping(value = "/create", method = { RequestMethod.GET })
    public String create( Model model) {
        Task task = new Task();
        LocalDateTime lt = LocalDateTime.now();
        task.setTime(lt);
        model.addAttribute("task", task);
        return "edit";
    }

    @RequestMapping(value = "/edit/{id}", method = { RequestMethod.GET })
    public String edit(@PathVariable("id") int id, Model model) throws IOException {
        model.addAttribute("task", FileStore.readTask(id));
        return "edit";
    }

    @PostMapping(value = "/edit")
    public String edit(@Valid @ModelAttribute Task task,
                       BindingResult bindingResult,
                       @RequestParam("startTimeString") String startTime,
                       @RequestParam("endTimeString") String endTime,
                       @RequestParam("repeatInterval") int interval,
                       Model model) throws IOException {
        String saveErrors = task.dateHandler(startTime, endTime, interval);
        model.addAttribute("task", task);
        if (bindingResult.hasErrors()) {
            List<FieldError> errors = bindingResult.getFieldErrors();
            String bindingResultMessage = "";
            for (FieldError error : errors ) {
                bindingResultMessage += error.getDefaultMessage();
            }
            logger.error("Задание №"+task.getId()+" - "+task.getTitle()+" не сохранено: "+ bindingResultMessage);
            return "edit";
        } else if(saveErrors.length() > 0) {
            model.addAttribute("saveErrors", saveErrors);
            logger.error("Задание №"+task.getId()+" - "+task.getTitle()+" не сохранено: "+ saveErrors);
            return "edit";
        } else {
            task.save();
            model.addAttribute("saveSuccess", "Задание успешно сохранено!");
            logger.info("Задание №"+task.getId()+" - "+task.getTitle()+" сохранено!");
            return "edit";
        }
    }

    @RequestMapping(value = "/delete/{id}", method = { RequestMethod.GET, RequestMethod.POST })
    public String delete(@PathVariable("id") int id, Model model) throws IOException {
        FileStore.delete(id);
        logger.warn("Задание №"+id+" было удалено!");
        model.addAttribute("tasks", FileStore.readList());
        return "list";
    }

}
