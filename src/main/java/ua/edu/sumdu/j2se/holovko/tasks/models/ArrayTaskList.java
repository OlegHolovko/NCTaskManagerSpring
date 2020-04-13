package ua.edu.sumdu.j2se.holovko.tasks.models;

import org.apache.commons.lang.builder.HashCodeBuilder;

import java.util.*;
import java.util.stream.Stream;

public class ArrayTaskList extends AbstractTaskList {
  private static final int DEFAULT_CAPACITY = 10;
  private Task[] tasks;
  private int size;
  private Iterator it;

  public ArrayTaskList() {
    size = 0;
    tasks = new Task[DEFAULT_CAPACITY];
    it = this.iterator();
  }

  public ArrayTaskList(Task[] tasks) {
    this.tasks = tasks;
    size = tasks.length;
    it = this.iterator();
  }

  public Task[] getTasks() {
    return tasks;
  }

  public void setTasks(Task[] tasks) {
    this.tasks = tasks;
    size = tasks.length;
  }

  public int size() {
    return size;
  }

  /**
   * Add object in array.
   * @param task - object Task
   */
  @Override
  public void add(Task task) {
    if (size == tasks.length) {
      increaseArray();
    }
    tasks[size] = task;
    size++;
    it.next();
  }

  // увеличить массив если в нем закончилось место
  private void increaseArray() {
    Task[] newArray = new Task[tasks.length * 2];
    System.arraycopy(tasks, 0, newArray, 0, size - 1);
    tasks = newArray;
  }

  private int indexOf(Task task) {
    for (int i = 0; i < size; i++) {
      if (tasks[i].equals(task)) {
        return i;
      }
    }
    return -1;
  }

  /**
   * Remove object from array.
   * @param task - object Task
   * @return - will returns true when object exists in array
   */
  @Override
  public boolean remove(Task task) {
    int currentIndex;
    currentIndex = indexOf(task);
    if (currentIndex > -1) {
      if(currentIndex+1 < size) {
        System.arraycopy(tasks, currentIndex + 1, tasks, currentIndex, size - 1);
      }
      else {
        System.arraycopy(tasks, currentIndex + 1, tasks, currentIndex, size - currentIndex);
      }
      size--;
      return true;
    } else {
      return false;
    }
  }

  @Override
  public boolean removeByIndex( int currentIndex) {
    if (currentIndex > -1) {
      if(currentIndex+1 < size) {
        System.arraycopy(tasks, currentIndex + 1, tasks, currentIndex, size - 1);
      }
      else {
        System.arraycopy(tasks, currentIndex + 1, tasks, currentIndex, size - currentIndex);
      }
      size--;
      return true;
    } else {
      return false;
    }
  }


  /**
   * Get task by index.
   * @param index - index of array
   * @return - will returns object by index
   * @throws IndexOutOfBoundsException - excepts when index less
   *      than zero or higher than index of array
   */
  @Override
  public Task getTask(int index) throws IndexOutOfBoundsException {
    if (index < 0 && index > this.size) {
      throw new IndexOutOfBoundsException("Index out of Bounds");
    }
    return tasks[index];
  }

  @Override
  public ArrayTaskList clone() throws CloneNotSupportedException {
      ArrayTaskList list = (ArrayTaskList)super.clone();
      list.tasks = this.tasks.clone();
    return list;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof ArrayTaskList)) return false;
    ArrayTaskList that = (ArrayTaskList) o;
    Iterator eqit1 = that.iterator();
    Iterator eqit2 = that.iterator();
    while (eqit1.hasNext()) {
      if (eqit1.next().hashCode() != eqit2.next().hashCode()) {
        return false;
      }
    }
    if (eqit1.hasNext() || eqit2.hasNext()) {
      // one of the iterators has more elements than the other
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    Iterator hashIt = this.iterator();
    int hashCode = 1;
    while (hashIt.hasNext()) {
      Task task = (Task) hashIt.next();
      if(task != null) {
        hashCode += task.hashCode();
      }
    }

    return new HashCodeBuilder(17, 37).
        append(hashCode).
        toHashCode();
  }

  @Override
  public String toString() {
    return "ArrayTaskList{"
        + "tasks=" + Arrays.toString(tasks)
        + ", size=" + size
        + '}';
  }

  @Override
  public Iterator<Task> iterator() {
    return new ArrayIterator();
  }

  private class ArrayIterator implements Iterator<Task> {
    private int currentIndex = 0;
    private int nextWasCalled = 0;

    public ArrayIterator() {
      currentIndex = 0;
    }

    @Override
    public boolean hasNext() {
      return currentIndex < size;
    }

    @Override
    public Task next() {
        nextWasCalled = 1;
        Task task = getTask(currentIndex);
        currentIndex++;
        return task;
    }

    @Override
    public void remove() {
      if(nextWasCalled == 0) {
        throw new IllegalStateException();
      }
      if(currentIndex <= size){
        ArrayTaskList.this.removeByIndex(--currentIndex);
      }
    }
  }

  public Stream<Task> getStream() {
    ArrayList list = new ArrayList();
    Iterator<Task> iterator = this.iterator();
    while (iterator.hasNext()) {
      list.add(iterator.next());
    }
    return list.stream();
  }
}
