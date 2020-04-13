package ua.edu.sumdu.j2se.holovko.tasks.models;

import org.apache.commons.lang.builder.HashCodeBuilder;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.stream.Stream;

public class LinkedTaskList extends AbstractTaskList {
  private Node head;
  private int size;
  private Iterator it;

  private static class Node implements Cloneable{
    private Node next;
    private Task task;

    Node(Task data) {
      task = data;
      next = null;
    }

    public Object getData() {
      return task;
    }

    @Override
    protected Node clone() throws CloneNotSupportedException {
      return (Node)super.clone();
    }
  }

  public LinkedTaskList() {
    size = 0;
    head = new Node(null);
    it = this.iterator();
  }

  public LinkedTaskList(Task[] tasks) {
    for (Task element: tasks) {
        add(element);
    }
  }



  /**
   * Add object in array.
   * @param task - object Task
   */

  @Override
  public void add(Task task) {
    Node temp = new Node(task);
    Node current = head;

    if (current != null) {
      for (int i = 0; i < size && current.next != null; i++) {
        current = current.next;
      }
    }

    temp.next = current.next;
    current.next = temp;
    size++;
  }

  public int size() {
    return size;
  }

  /**
   * Remove object from array.
   * @param task - object Task
   * @return - will returns true when object exists in array
   */

  @Override
  public boolean remove(Task task)
  {
      Node current = head, prev = null;

      /**
       * For null
       */
      if (current == null) {
          return false;
      }

      /**
       * For single
       */
      if (current != null && task.equals(current.task)) {
          head = current.next;
          size--;
          return true;
      }

      /**
       * For many
       */
      while (current != null && !task.equals(current.task)) {
          prev = current;
          current = current.next;
      }

      if (current != null) {
          prev.next = current.next;
          size--;
          return true;
      }

      return false;
  }

  @Override
  public boolean removeByIndex(int currentIndex) {
    Node current = head, prev = null;

    // CASE 1:
    // If index is 0, then head node itself is to be deleted

    if (currentIndex == 0 && current != null) {
      head = current.next; // Changed head
      return true;
    }

    //
    // CASE 2:
    // If the index is greater than 0 but less than the size of LinkedList
    //
    // The counter
    int counter = 0;

    // Count for the index to be deleted,
    // keep track of the previous node
    // as it is needed to change currNode.next
    while (current != null) {
      if (counter == currentIndex) {
        // Since the currNode is the required position
        // Unlink currNode from linked list
        prev.next = current.next;
        size--;
        return true;
      }
      else {
        // If current position is not the index
        // continue to next node
        prev = current;
        current = current.next;
        counter++;
      }
    }

    // If the position element was found, it should be at currNode
    // Therefore the currNode shall not be null
    //
    // CASE 3: The index is greater than the size of the LinkedList
    //
    // In this case, the currNode should be null
    if (current == null) {
      // Display the message
      return false;
    }

    // return the List
    return false;
  }

  /**
   * Get task by index.
   * @param index - index of array
   * @return - will returns object by index
   * @throws IndexOutOfBoundsException - excepts when index less
   *      than zero or higher than index of array
   */
  @Override
  public Task getTask(int index) throws IndexOutOfBoundsException
  {
    if (index < 0) {
      return null;
    }
    Node current = null;
    if (head != null) {
      current = head.next;
      for (int i = 0; i < index; i++) {
        if (current.next == null) {
          return null;
        }
        current = current.next;
      }
      return current.task;
    }
    throw new IndexOutOfBoundsException("Index out of Bounds");
  }

  @Override
  public LinkedTaskList clone() throws CloneNotSupportedException {
      LinkedTaskList list = (LinkedTaskList)super.clone();
      list.head = this.head.clone();
      return list;
  }


    @Override
  public String toString() {
    String output = "";

    if (head != null) {
      Node temp = head.next;
      while (temp != null) {
          if(temp.task != null) {
              output += "[" + temp.task.toString() + "]";
          }
          else{
              output += "[]";
          }
        temp = temp.next;
      }

    }
    return output;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof LinkedTaskList)) return false;
    LinkedTaskList that = (LinkedTaskList) o;
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
      else {
        hashCode += 31;
      }
    }

    return new HashCodeBuilder(17, 37).
        append(hashCode).
        toHashCode();
  }

  @Override
  public Iterator<Task> iterator() {
    return new LinkedTaskList.LinkedIterator();
  }

  private class LinkedIterator implements Iterator<Task> {
    public int currentIndex;
    private int nextWasCalled;
    private Node node;
    private Task currentTask;

    LinkedIterator() {
      currentIndex = 0;
      this.node = head;
    }

    @Override
    public boolean hasNext() {
      return size != 0 && currentIndex < size;
    }

    @Override
    public Task next() {
      nextWasCalled = 1;
      if (node == null)
        throw new NoSuchElementException();
      else {
        if (currentIndex == 0) {
          node = node.next;
        }
        currentTask = (Task) node.task;
        node = node.next;
        currentIndex++;
        return currentTask;
      }
    }

    @Override
    public void remove() {
      if(nextWasCalled == 0) {
        throw new IllegalStateException();
      }
      LinkedTaskList.this.remove(currentTask);
      currentTask = null;
    }
  }

  public Stream<Task> getStream() {
    LinkedList list = new LinkedList();
    Iterator<Task> iterator = this.iterator();
    while (iterator.hasNext()) {
      list.add(iterator.next());
    }
    return list.stream();
  }

}
