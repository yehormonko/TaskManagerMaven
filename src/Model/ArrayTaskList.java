package Model;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.NoSuchElementException;
/**
*class ArrayTaskList for containing tasks, based on array
*@author YehorMonko
*@version With_Maven_and_log4j
 */
public class ArrayTaskList implements Iterable<Task>{
    private int size = 1;
    private Task[] tasks = new Task[size];
    private int pointer = 0;
    private Exception nullTaskexeption = new Exception("Task can't be null");

/**
 * for adding task to list
 * @param task task you want to add
 */
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

/**
 * for removing task from list, return true if task was removed or false if there is no such task
 * @param task task you want to remove
 * @return true if task was removed or false if there is no such tasks
 */
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

/**
 * for getting size of list
 * @return size of list
 */
    public int size() {
        return pointer;
    }

/**
 * return task from list by its index, can be error if index is bigger or equal to size of list
 * @param index index of the task, should be smaller than size
 * @return task which has that index
 */
    public Task getTask(int index) { return tasks[index]; }

/**
 * iterator for that type of list has 3 methods: hasNext - check if there is next task after current, next - return next
 * task after current and remove - delete current task
 * @return new iterator fo ArrayTaskList
 */
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

/**
 * return ArrayTaskList which contain tasks that should be done from date "from" to date "to", if there is no such tasks-
 * return empty list
 * @param from begin of period in which tasks should be done
 * @param to end of period in which tasks should be done
 * @return list which contain tasks that should be done in period
 */
public ArrayTaskList incoming(Date from, Date to) {
    ArrayTaskList taskList;
    taskList = new ArrayTaskList();
    for (int i = 0; i < this.size(); i++) {
        Task temp = this.getTask(i);
        if((temp.nextTimeAfter(from)!=null)){
            if (temp.nextTimeAfter(from).before(to)) {
                taskList.add(this.getTask(i));
            }}
    }
    return taskList;
}

/**
 * describe list, using all params and return that information in string
 * @return string which describe list, also show all elements
 */
    @Override
    public String toString() {
        String rezult = "ArrayTaskList{size=" + pointer + ", tasks:";
        for (int i = 0; i < pointer; i++) {
            rezult += "\n" + tasks[i].toString();
        }
        rezult +=  " }";
        return rezult;
    }

/**
 * for comparing list with object, return true if they equal, in other variant - return false
 * @param o  object with which list compares
 * @return true if list and object are equal or false if not
 */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ArrayTaskList that = (ArrayTaskList) o;
        if (pointer != that.pointer) return false;
        return Arrays.equals(tasks, that.tasks);
    }

/**
 * make hashcode of list
 * @return hashcode of ArrayTaskList
 */
    @Override
    public int hashCode() {
        int result = Arrays.hashCode(tasks);
        result = 31 * result + pointer;
        return result;
    }
}
