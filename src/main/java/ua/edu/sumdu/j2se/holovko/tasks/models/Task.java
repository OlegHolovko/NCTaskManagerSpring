package ua.edu.sumdu.j2se.holovko.tasks.models;

import org.apache.commons.lang.builder.HashCodeBuilder;
import ua.edu.sumdu.j2se.holovko.tasks.models.storage.FileStore;

import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import javax.validation.constraints.*;

public class Task implements Cloneable, Serializable {
  private int id;
  @NotEmpty(message = "Название не должно быть пустым.")
  private String title;
  private LocalDateTime time;
  private LocalDateTime start;
  private LocalDateTime end;
  @Min(value = 0, message = "Только положительное число.")
  private int interval;
  private boolean active;
  private boolean repeated;
  private static final long serialVersionUID = 1;

  public Task() {
      id = 0;
  }

    /**
   * Constructor creates non-active task which has executed
   * in given time without repeats with given title.
   *
   * @param title - task name
   * @param time - executing time
   */
  public Task(String title, LocalDateTime time) throws IllegalArgumentException, IOException {
    id = 0;
    setTime(time);
    this.title = title;
    this.active = false;
  }

    /**
   * Constructor creates non-active task which has executed
   * in given time interval (start time and end time inclusive)
   * with given title.
   *
   * @param title - task name
   * @param start - start time
   * @param end - end time
   * @param interval - time interval
   */
  public Task(String title, LocalDateTime start, LocalDateTime end, int interval) throws IOException {
    id = 0;
    setTime(start, end, interval);
    this.title = title;
    this.active = false;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public boolean isActive() {
    return active;
  }

  public void setActive(boolean active) {
    this.active = active;
  }

  public int getId() {
    return id;
  }

  public void setId(int id){
      this.id = id;
  }

  public LocalDateTime getTime() {
    if(repeated) {
        return LocalDateTime.from(start);
    } else {
        return LocalDateTime.from(time);
    }
  }

  /**
   *  Method sets executing time for non-repeated task.
   *  In case task was repeated it will be made non-repeated.
   *
   * @param time - executing time
   */
  public void setTime(LocalDateTime time) {
    this.time = time;
    this.start = time;
    this.end = time;
    if(this.repeated){
      this.repeated = false;
      this.interval = 0;
    }
  }

  /**
   * Method sets executing time parameters for repeated task.
   * In case task was non-repeated it will be made repeated.
   *
   * @param start - start time
   * @param end - end time
   * @param interval - time interval
   */
  public void setTime(LocalDateTime start, LocalDateTime end, int interval) {
    this.start = start;
    this.time = start;
    this.end = end;
    if(!this.repeated) {
        this.interval = interval;
        this.repeated = true;
    }
  }

  public LocalDateTime getStartTime() {
    if(!this.repeated) {
        return LocalDateTime.from(this.time);
    } else {
        return LocalDateTime.from(this.start);
    }
  }

  public LocalDateTime getEndTime() {
      if(!this.repeated) {
          return LocalDateTime.from(this.time);
      } else {
          return LocalDateTime.from(this.end);
      }
  }

  public int getRepeatInterval() {
      if(!this.repeated) {
          return 0;
      } else {
          return this.interval;
      }
  }

  public String getStartTimeString(){
    if(!this.repeated) {
      if(this.time == null) {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));
      } else {
        return this.time.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));
      }
    } else {
      if(this.start == null) {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));
      } else {
        return this.start.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));
      }
    }
  }

  public String getEndTimeString(){
    if(!this.repeated) {
      if(this.time == null) {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));
      } else {
        return this.time.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));
      }
    } else {
      if(this.end == null) {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));
      } else {
        return this.end.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));
      }
    }
  }

  /**
   * Method sets executing time parameters.
   *
   * @param start - start time
   * @param end - end time
   * @param interval - time interval
   */

  public void setInterval(LocalDateTime start, LocalDateTime end, int interval) {
    this.start = start;
    this.end = end;
    this.interval = interval;
  }

  public boolean isRepeated() {
    return this.repeated;
  }

  public void setRepeated(boolean repeated) {
    this.repeated = repeated;
  }

  /**
   * Method returns next executing time or -1 in case task is`n executing.
   *
   * @param current - executing time
   * @return next executing time or -1 in case task is`n executing.
   */
  public LocalDateTime nextTimeAfter(LocalDateTime current) {
    if (current == null) {
      throw new IllegalArgumentException();
    }
    if (!isActive()) {
      return null;
    }

    if (!isRepeated()) {
      return current.isBefore(time) ? LocalDateTime.from(this.time) : null;
    } else if (current.isBefore(start)) {
      return LocalDateTime.from(this.start);
    } else if (current.isBefore(end)) {
      LocalDateTime tempTime = LocalDateTime.from(start);
      while (current.compareTo(tempTime) >= 0) {
        tempTime = tempTime.plusHours(interval);
        //tempTime = tempTime.plusSeconds(interval);
      }
      return tempTime.compareTo(end) <= 0 ? tempTime : null;
    } else {
      return null;
    }
  }

  @Override
  public String toString() {
    return "Task{" +
            "id='" + id + '\'' +
            "title='" + title + '\'' +
            "active='" + active + '\'' +
            "repeated='" + repeated + '\'' +
            "start='" + start + '\'' +
            "end='" + end + '\'' +
            "interval='" + interval + '\'' +
            '}';
  }

  @Override
  public Task clone() throws CloneNotSupportedException {
    Task task =  (Task)super.clone();
    return task;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Task)) return false;
    Task task = (Task) o;
    return getTime() == task.getTime() &&
        start == task.start &&
        end == task.end &&
        interval == task.interval &&
        isActive() == task.isActive() &&
        isRepeated() == task.isRepeated() &&
        Objects.equals(getTitle(), task.getTitle());
  }

  @Override
  public int hashCode() {

      return new HashCodeBuilder(17, 37).
          //append(getTitle()).
          append(getTime()).
          append(getStartTime()).
          append(getEndTime()).
          append(getRepeatInterval()).
          append(isActive()).
          append(isRepeated()).
              toHashCode();
  }

  public String dateHandler(String startTime, String endTime, int interval){
    String saveErrors = "";

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
    LocalDateTime startTimeFormatted = LocalDateTime.parse(startTime, formatter);
    LocalDateTime endTimeFormatted = LocalDateTime.parse(endTime, formatter);
    if(startTimeFormatted != null && endTimeFormatted != null && interval != 0) {
      if(endTimeFormatted.isAfter(startTimeFormatted))
        this.setTime(startTimeFormatted, endTimeFormatted, interval);
      if(endTimeFormatted.isBefore(startTimeFormatted))
        saveErrors = "Дата окончания наступает раньше даты начала. ";
      if(endTimeFormatted.isEqual(startTimeFormatted))
        saveErrors = "Дата окончания равна дате начала. ";
    }
    else {
      this.setTime(startTimeFormatted);
    }
    return saveErrors;
  }

  public void save() throws IOException {
    FileStore.writeTask(this);
  }
}
