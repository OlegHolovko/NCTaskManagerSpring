package ua.edu.sumdu.j2se.holovko.tasks.models;

import com.google.gson.Gson;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Tasks {
  public static Iterable<Task> incoming(Iterable<Task> tasks, LocalDateTime start, LocalDateTime end) {
    AbstractTaskList currentList = TaskListFactory.createTaskList(tasks);
    for (Task task : tasks) {
      if (task.nextTimeAfter(start) != null
          && task.nextTimeAfter(start).compareTo(end) <= 0
          && task.isActive()) {
        currentList.add(task);
      }
    }
    return currentList;
  }

  public static SortedMap<LocalDateTime, Set<Task>> calendar(Iterable<Task> tasks, LocalDateTime start, LocalDateTime end) {
    SortedMap<LocalDateTime, Set<Task>> currentMap = new TreeMap<>();
    Iterable<Task> workList = incoming(tasks, start, end);

    for (Task task : workList) {
      LocalDateTime time = task.nextTimeAfter(start);
      while (time != null && end.compareTo(time) >= 0) {
        if (!currentMap.containsKey(time)) {
          Set<Task> set = new HashSet<>();
          set.add(task);
          currentMap.put(time, set);
        } else {
          currentMap.get(time).add(task);
        }
        time = task.nextTimeAfter(time);
      }
    }
    return currentMap;
  }

  public static List<Map> fullcalendar(Iterable<Task> tasks, LocalDateTime start, LocalDateTime end) {
    List<Map> listEvents = new ArrayList<>();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    Iterable<Task> workList = incoming(tasks, start, end);
    for (Task element : workList) {
      LocalDateTime time = element.nextTimeAfter(start);
      int itr=0;
      while (time != null && end.compareTo(time) >= 0) {
        itr++;
        Map<String, String> mapEvents = new TreeMap<>();
        mapEvents.put("title", element.getTitle()+" #"+itr);
        mapEvents.put("start", time.format(formatter));
        mapEvents.put("end", time.plusHours(element.getRepeatInterval()).format(formatter));
        if(element.isActive()) {
          mapEvents.put("active", "true");
        } else {
          mapEvents.put("active", "false");
        }
        if(element.isRepeated()) {
          mapEvents.put("repeated", "true");
        } else  {
          mapEvents.put("repeated", "false");
        }
        mapEvents.put("interval", String.valueOf(element.getRepeatInterval()));
        mapEvents.put("url", "#");
        time = element.nextTimeAfter(time);
        listEvents.add(mapEvents);
      }
    }
    return listEvents;
  }

}
