package ua.edu.sumdu.j2se.holovko.tasks.models;

public class TaskListFactory {
    public TaskListFactory() {
    }

    public static AbstractTaskList createTaskList(ListTypes.types type){
        if (ListTypes.types.ARRAY.equals(type)) {
            return new ArrayTaskList();
        } else if (ListTypes.types.LINKED.equals(type)) {
            return new LinkedTaskList();
        }
        return null;
    }

    public static AbstractTaskList createTaskList(Iterable<Task> o){
        if(o instanceof ArrayTaskList){
            return TaskListFactory.createTaskList(ListTypes.types.ARRAY);
        }
        else if (o instanceof LinkedTaskList) {
            return TaskListFactory.createTaskList(ListTypes.types.LINKED);
        }
        else {
            return TaskListFactory.createTaskList(ListTypes.types.ARRAY);
        }
    }
}
