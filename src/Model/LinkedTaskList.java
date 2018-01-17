package Model;
import java.io.*;
import java.util.Iterator;
import java.util.NoSuchElementException;

class Link {
    private Task task;
    private Link next;

     Link(Task task) {
        this.task = task;
    }

     Task getTask() { return task; }

     Link getNext() { return next; }

     void setNext(Link next) { this.next = next; }
}
public class LinkedTaskList extends TaskList implements Cloneable, Iterable<Task> {
    private int size = 0;
    private  Link first;

    public LinkedTaskList() {
        first = null;
    }

    public void add(Task task) {
        Link newLink = new Link(task); 
        newLink.setNext(first);
        first = newLink;
        size++;
    }

    public int  size() { return size; }

    public Task getTask(int index) {
        Link element = first;
        int number = 0;
        while (element != null || number < index) {
            if (index == number) {
                return element.getTask();
            }
            element = element.getNext();
            number++;
        }
        return null;
    }

    public boolean remove(Task key) {
        Link current = first;
        Link prev = null;
        boolean ret = false;
        while (current != null) { 
            if (prev != null && current.getNext() != null && current.getTask().equals(key)) {
                prev.setNext(current.getNext());
                ret = true;
                break;
            }
            else if (prev == null && current.getTask().equals(key)) {
                first = current.getNext();
                ret = true;
                break;
            }
            else if (current.getNext() == null && current.getTask().equals(key)) {
                prev.setNext(null);
                ret = true;
                break;
            }
            else {
                prev = current;
                current = current.getNext();
            }
        }
        if (ret) {
        size = size - 1;
        }
        return ret;
    }

    @Override
    public Iterator<Task> iterator() {
        return new Iterator<Task>() {
             private boolean useNext = false;
            private int cursor = 0;
            private boolean useFirst = false;

            public boolean hasNext() {
                int cur = cursor + 1;
                return (LinkedTaskList.this.getTask(cur) != null);
            }

            public Task next() {
             useNext = true;
                if (!useFirst) { useFirst = true; return LinkedTaskList.this.getTask(0); }
                if (this.hasNext())
                    return LinkedTaskList.this.getTask(++cursor);
                else throw new IllegalStateException();
            }

            public void remove() {
            if (!useNext) throw new IllegalStateException();
               else LinkedTaskList.this.remove(LinkedTaskList.this.getTask(cursor--));
            }
        };

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LinkedTaskList that = (LinkedTaskList) o;
        if (size != that.size) return false;
        boolean ret = true;
        Link element = first;
        Link element2   = that.first;
        while (element != null) {
            if (!element.getTask().equals(element2.getTask())) {
                ret = false;
                break;
            }
            element = element.getNext();
            element2 = element2.getNext();
        }
        return ret;
    }

    @Override
    public int hashCode() {
        int ret = 0;
        Link element = first;
        while (element.getNext() != null) {
            ret = 31 * element.getTask().hashCode() + ret;
            element = element.getNext();
        }
        return ret;
    }

    @Override
    public String toString() {
        String rezult = "LinkedTaskList{size = " + size + ", tasks:";
        for (int i = 0; i < size; i++) {
            rezult += "\n" + this.getTask(i).toString();
        }
        return rezult + " }";
    }

    public static  void ref(LinkedTaskList list) {
        Task[] tasks = new Task[list.size()];
        for (int i = 0; i < tasks.length; i++) {
            tasks[i] = list.getTask(i);
        }
        for (int i = 0; i < tasks.length; i++) {
            list.remove(tasks[i]);
        }
        for (int i = 0; i < tasks.length; i++) {
            list.add(tasks[i]);
        }
    }
}