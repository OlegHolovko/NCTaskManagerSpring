package ua.edu.sumdu.j2se.holovko.tasks.models;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.stream.Stream;

public abstract class AbstractTaskList implements Iterable<Task>, Cloneable, Serializable {
    public abstract void add(Task task);
    public abstract boolean remove(Task task);
    public abstract boolean removeByIndex(int currentIndex);
    public abstract int size();
    public abstract Task getTask(int index);
    public abstract Stream<Task> getStream();
/*
    public final AbstractTaskList incoming(LocalDateTime from, LocalDateTime to) {
        AbstractTaskList taskList;
        if(this instanceof ArrayTaskList){
            taskList = TaskListFactory.createTaskList(ListTypes.types.ARRAY);
        }
        else if (this instanceof LinkedTaskList) {
            taskList = TaskListFactory.createTaskList(ListTypes.types.LINKED);
        }
        else {
            taskList = TaskListFactory.createTaskList(ListTypes.types.ARRAY);
        }

        // first variant
        for(int i=0; i < taskList.size(); i++){
            if(this != null && this.getTask(i).isActive()
                && this.getTask(i).nextTimeAfter(from) > from
                && this.getTask(i).nextTimeAfter(to) < to) {
                taskList.add(this.getTask(i));
            }
        }

        //second variant with stream
        this.getStream().filter(task -> task.nextTimeAfter(from) != null
            && task.getEndTime().compareTo(to) && task.isActive()).limit(taskList.size()).forEach(taskList::add);

        return taskList;
    }
    */


}
