package ua.edu.sumdu.j2se.holovko.tasks.models;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class TaskIO {
    static ZoneId zoneId = ZoneId.systemDefault();
    public static void write(AbstractTaskList tasks, OutputStream out) throws IOException{
        DataOutputStream dataOut = new DataOutputStream(out);
        try {
            dataOut.writeInt(tasks.size());
            for (Task task : tasks) {
                dataOut.writeInt(task.getTitle().length());
                dataOut.writeInt(task.getId());
                //dataOut.writeChars(task.getTitle());
                dataOut.writeUTF(task.getTitle());
                dataOut.writeBoolean(task.isActive());
                dataOut.writeInt(task.getRepeatInterval());
                dataOut.writeLong(task.getStartTime().atZone(zoneId).toInstant().toEpochMilli());
                if (task.getRepeatInterval() != 0) {
                    dataOut.writeLong(task.getEndTime().atZone(zoneId).toInstant().toEpochMilli());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            dataOut.flush();
            dataOut.close();
        }
    }

    public static void read(AbstractTaskList tasks, InputStream in) throws IOException {
        DataInputStream dataIn = new DataInputStream(in);
        try{
            int countTask = dataIn.readInt();
            for(int i = 1; i <= countTask; i++){
                Task task;
                int lengthTitle = dataIn.readInt();
                //byte[] array = new byte[lengthTitle];
                //String title = String.valueOf(dataIn.read(array));
                int id = dataIn.readInt();
                String title = dataIn.readUTF();
                boolean isActive = dataIn.readBoolean();
                int repeatInterval = dataIn.readInt();
                LocalDateTime start = Instant.ofEpochMilli(dataIn.readLong()).atZone(zoneId).toLocalDateTime();
                task = new Task(title, start);
                task.setId(id);
                task.setActive(isActive);
                if (repeatInterval != 0) {
                    LocalDateTime end = Instant.ofEpochMilli(dataIn.readLong()).atZone(zoneId).toLocalDateTime();
                    task.setTime(start, end, repeatInterval);
                }
                tasks.add(task);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            dataIn.close();
        }
    }

    public static void writeBinary(AbstractTaskList tasks, File file) throws IOException {
        FileOutputStream fileOut = new FileOutputStream(file);
        try {
            write(tasks,fileOut);
        } finally {
            fileOut.close();
        }
    }

    public static void readBinary(AbstractTaskList tasks, File file) throws IOException {
        FileInputStream fileIn = new FileInputStream(file);
        try {
            read(tasks, fileIn);
        } finally {
            fileIn.close();
        }
    }

    public static void write(AbstractTaskList tasks, Writer out) throws IOException {
        Gson gson = new GsonBuilder().registerTypeAdapter(ArrayTaskList.class, new ArrayListSerializer()).create();
        String json = gson.toJson(tasks);
        try (BufferedWriter writer = new BufferedWriter(out)) {
            writer.write(json);
            writer.flush();
        }
    }

    public static void read(AbstractTaskList tasks, Reader in) {
        try (BufferedReader reader = new BufferedReader(in)) {
            String text;
            while ((text = reader.readLine()) != null) {
                AbstractTaskList taskList = new Gson().fromJson(text, tasks.getClass());
                for (Task task : taskList) {
                    tasks.add(task);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void writeText(AbstractTaskList tasks, File file) {
        String json = new Gson().toJson(tasks);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(json);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void readText(AbstractTaskList tasks, File file) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String text;
            while ((text = reader.readLine()) != null) {
                AbstractTaskList taskList = new Gson().fromJson(text, tasks.getClass());
                for (Task task : taskList) {
                    tasks.add(task);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
