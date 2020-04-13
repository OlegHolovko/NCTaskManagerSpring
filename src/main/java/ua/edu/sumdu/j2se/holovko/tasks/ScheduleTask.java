package ua.edu.sumdu.j2se.holovko.tasks;

import com.google.gson.Gson;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ua.edu.sumdu.j2se.holovko.tasks.config.GlobalVars;
import ua.edu.sumdu.j2se.holovko.tasks.models.AbstractTaskList;
import ua.edu.sumdu.j2se.holovko.tasks.models.ArrayTaskList;
import ua.edu.sumdu.j2se.holovko.tasks.models.Task;
import ua.edu.sumdu.j2se.holovko.tasks.models.TaskIO;
import ua.edu.sumdu.j2se.holovko.tasks.notification.Notification;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.TreeMap;

@Service
public class ScheduleTask {
    final static Logger logger = Logger.getLogger(ScheduleTask.class);

    @Autowired
    private Notification webSocketNotification;

    @Autowired
    private Notification emailNotification;

    @Scheduled(fixedRate = 10000)
    public void notificator() throws IOException {
        Gson gson = new Gson();
        Map<Integer,String> alerts = new TreeMap<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd-MM-yyyy");
        AbstractTaskList taskList = new ArrayTaskList();
        File file = new File(GlobalVars.dataFilePath);
        if (file.exists()) {
            TaskIO.readBinary(taskList, file);
            for(Task element : taskList){
                LocalDateTime timeExecution = element.nextTimeAfter(LocalDateTime.now());
                if(timeExecution != null){
                    alerts.put(element.getId(),"Задание <strong>"+element.getTitle()+"</strong> будет выполнено в "+
                            timeExecution.format(formatter)+"!");

                    LocalDateTime now = LocalDateTime.now();
                    Duration duration = Duration.between(now, timeExecution);
                    long diff = Math.abs(duration.toMillis());
                    if(diff < 10000){
                        //emailNotification.sendNotification("Внимание! В "+timeExecution.format(formatter)
                                //+" начинается выполнение задания "+element.getTitle());
                        logger.info("Внимание! В "+timeExecution.format(formatter)
                                +" начинается выполнение задания "+element.getTitle());
                    }
                }
            }
            String json = gson.toJson(alerts);
            webSocketNotification.sendNotification(json);
        }
    }

}