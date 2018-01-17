package Model;
import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class ArrayTaskList extends TaskList {
    private int size = 1;
    private Task[] tasks = new Task[size];
    private int pointer = 0;
    private Exception nullTaskexeption = new Exception("Task can't be null");

    public void add(Task task) {
        if (task == null) {
            try {
                 throw nullTaskexeption;
            } catch (Exception e) {
                System.out.println("Task cant be null");
            }
        }
        this.resize(1);
        tasks[pointer] = task;
        pointer++;
    }

    public boolean remove(Task task) {
        boolean ret = false;
        int index =- 1;
        for (int i = 0; i < pointer; i++) {
            if  (tasks[i].equals(task)) {
               index = i;
               ret = true;
            }
        }
        if (index >= 0) {
            for (int i = index; i < pointer; i++) {
                tasks[i] = tasks[i + 1];
            }
            pointer--;
            this.resize(-1);
        }
        return ret;
    }

    private void resize(int number) {
        Task[] newTasks = new Task[tasks.length + number];
        System.arraycopy(tasks, 0, newTasks, 0, pointer);
        tasks = newTasks;
    }

    public int size() {
        return pointer;
    }

    public Task getTask(int index) { return tasks[index]; }

    public Iterator<Task> iterator() {
        return new Iterator<Task>() {
            private int cursor = 0;
            boolean useNext = false;

            public boolean hasNext() {
                if (cursor < pointer) {
                    return true;
                }
                return false;
            }

            public Task next() {
                useNext = true;
                if (this.hasNext()) {
                    return ArrayTaskList.this.getTask(cursor++);
                    }
                else throw new NoSuchElementException();
            }
            
           public void remove() {
                if (!useNext) throw new IllegalStateException();
                else {
                ArrayTaskList.this.remove(ArrayTaskList.this.getTask(--cursor));
                }
            }
        };
    }

    @Override
    public String toString() {
        String rezult = "ArrayTaskList{size=" + pointer + ", tasks:";
        for (int i = 0; i < pointer; i++) {
            rezult += "\n" + tasks[i].toString();
        }
        rezult +=  " }";
        return rezult;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ArrayTaskList that = (ArrayTaskList) o;
        if (pointer != that.pointer) return false;
        return Arrays.equals(tasks, that.tasks);
    }

    @Override
    public int hashCode() {
        int result = Arrays.hashCode(tasks);
        result = 31 * result + pointer;
        return result;
    }
}
