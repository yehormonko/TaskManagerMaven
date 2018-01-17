package Model;
import java.util.Date;
import java.util.Iterator;

public abstract class TaskList implements Iterable<Task> {
    public abstract void add(Task task);
    public abstract boolean remove(Task task);
    public abstract int size();
    public abstract Task getTask(int index);
    public TaskList incoming(Date from, Date to) {
        TaskList taskList;
        if (this.getClass() == LinkedTaskList.class) taskList = new LinkedTaskList();
        else taskList = new ArrayTaskList();
        for (int i = 0; i < this.size(); i++) {
            Task temp = this.getTask(i);
            if((temp.nextTimeAfter(from)!=null)){
            if (temp.nextTimeAfter(from).before(to)) {
                taskList.add(this.getTask(i));
            }}
       }
        return taskList;
    }
    public abstract Iterator<Task> iterator();
}
