package ua.edu.sumdu.j2se.holovko.tasks.notification;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class WebSocketNotification implements Notification {
    @Autowired
    private SimpMessagingTemplate template;
    @Override
    public void sendNotification(String message){
        template.convertAndSend("/schedule/task", message);
    }
}
