package ua.edu.sumdu.j2se.holovko.tasks.models.storage;

import ua.edu.sumdu.j2se.holovko.tasks.config.GlobalVars;
import ua.edu.sumdu.j2se.holovko.tasks.models.AbstractTaskList;
import ua.edu.sumdu.j2se.holovko.tasks.models.ArrayTaskList;
import ua.edu.sumdu.j2se.holovko.tasks.models.Task;
import ua.edu.sumdu.j2se.holovko.tasks.models.TaskIO;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileStore{

    public static AbstractTaskList readList() throws IOException {
        AbstractTaskList taskList = new ArrayTaskList();
        File file = new File(GlobalVars.dataFilePath);
        if (!file.exists()) {
            file.createNewFile();
        }
        TaskIO.readBinary(taskList, file);
        return taskList;
    }

    public static Task readTask(int id) throws IOException {
        AbstractTaskList taskList = new ArrayTaskList();
        File file = new File(GlobalVars.dataFilePath);
        TaskIO.readBinary(taskList, file);
        for(Task task : taskList){
            if(task.getId() == id){
                return task;
            }
        }
        return null;
    }

    public static void writeList(AbstractTaskList taskList) throws IOException {
        File file = new File(GlobalVars.dataFilePath);
        if (!file.exists()) {
            file.createNewFile();
        }
        TaskIO.writeBinary(taskList, file);
    }

    public static void writeTask(Task taskForSave) throws IOException {
        AbstractTaskList taskList = new ArrayTaskList();
        AbstractTaskList taskListProcessed = new ArrayTaskList();
        File file = new File(GlobalVars.dataFilePath);
        TaskIO.readBinary(taskList, file);
        for(Task task : taskList){
            if(task.getId() == taskForSave.getId()) {
                taskListProcessed.add(taskForSave);
            }
            else{
                taskListProcessed.add(task);
            }
        }
        if(taskForSave.getId() == 0){
            taskForSave.setId(getLastId(taskList)+1);
            taskListProcessed.add(taskForSave);
        }
        clear();
        TaskIO.writeBinary(taskListProcessed, file);
    }

    public static void delete(int id) throws IOException {
        AbstractTaskList taskList = new ArrayTaskList();
        AbstractTaskList taskListProcessed = new ArrayTaskList();
        File file = new File(GlobalVars.dataFilePath);
        TaskIO.readBinary(taskList, file);
        for(Task task : taskList){
            if(task.getId() != id) {
                taskListProcessed.add(task);
            }
        }
        clear();
        TaskIO.writeBinary(taskListProcessed, file);
    }

    private static void clear() throws IOException {
        new FileOutputStream(GlobalVars.dataFilePath).close();
    }

    private static int getLastId(AbstractTaskList taskList) throws IOException {
        int maxId = 0;
        for(Task task : taskList){
            if(task.getId() > maxId){
                maxId = task.getId();
            }
        }
        return maxId;
    }
}
