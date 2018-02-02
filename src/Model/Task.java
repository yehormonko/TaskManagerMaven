package Model;
import java.util.Date;
/**
*class Task if an main element of task manager, constructed from name, date(s) and interval
* could be repeated or not repeated, active or inactive
*@author YehorMonko
*@version With_Maven_and_log4j
*/
public class Task implements Cloneable {

    private String title = null;
    private Date time = null;
    private Date start = null;
    private Date end = null;
    private int interval;
    private boolean active;
    private boolean repeated;

/**
 * empty constructor
 */
    public Task() {}

/**
 * constructor for not repeated task
 * @param title title of the task
 * @param time time of the task
 */
public Task(String title, Date time) {
        this.title = title;
        this.time = (Date) time.clone();
        repeated = false;
    }

/**
 * constructor for repeated task
 * @param title title of the task
 * @param start start of the task
 * @param end end of the task
 * @param interval interval of the task
 */
    public Task(String title, Date start, Date end, int interval) {
        this.title = title;
        this.interval = interval;
        this.start = (Date) start.clone();
        this.end = (Date) end.clone();
        repeated = true;
    }

/**
 * return title of the Task
 * @return tittle of task
 */
public String getTitle() {
        return title;
    }

/**
 * for setting tasks title
 * @param title title of the task
 */
    public void setTitle(String title) {
        this.title = title;
    }

/**
 * for gettinf activity of task
 * @return activity of task
 */
    public boolean isActive() {
        return active;
    }

/**
 * make task active ir inactive
 * @param active true or false - active or not
 */
    public void setActive(boolean active) {
        this.active = active;
    }

/**
 * get time of task if not repeated and start if repeated
 * @return time if nit repeated or start if repeated
 */
    public Date getTime() {
        if (!repeated) {
            return time;
        } else {
            return start;
        }
    }

/**
 * set new date if not repeated or make task not repeated and set time to it
 * @param time new time for task
 */
    public void setTime(Date time) {
        if (repeated) {
            repeated = false;
            this.time = (Date) time.clone();
        }
        else this.time = (Date) time.clone();
    }

/**
 * for getting start time if repeated and time if not repeated
 * @return start if repeated and time if not
 */
    public Date getStartTime() {
        if (repeated) return start;
        else return time;
    }
/**
 * for getting end time if repeated and time if not repeated
 * @return end if repeated and time if not
 */
    public Date getEndTime() {
        if (repeated) return end;
        else return time;
    }

/**
 * for getting repeat interval if repeated, if not - return 0
 * @return interval of task or 0 if not repeated
 */
    public int getRepeatInterval() {
        if (repeated) return interval;
        else return 0;
    }

/**
 * for making task not repeated and setting start, end and repeat interval. or changing that parameters if not repeated
 * @param start new start of task
 * @param end new end of task
 * @param interval new interval of task
 */
    public void setTime(Date start, Date end, int interval) {
        if (repeated) {
            this.start = (Date) start.clone();
            this.end = (Date) end.clone();
            this.interval = interval;
        } else {
            repeated = true;
            this.setTime(start, end, interval);
        }
    }

/**
 * is for changing type of task. if use it without changing dates - can be errors
 * @param repeated true - make repeated, false - not repeated
 */
public void setRepeated(boolean repeated){
        this.repeated= repeated;
}

/**
 * return type of task(repeated or not)
 * @return true if repeated and false if not
 */
public boolean isRepeated() {
        return  repeated;
    }

/**
 * set new start to the task, if not repeated - it will not change anything
 * @param start new start for a task
 */
public void setStart(Date start) {
        this.start = (Date) start.clone();
    }
/**
 * set new end to the task, if not repeated - it will not change anything
 * @param end new end for a task
 */
    public void setEnd(Date end) {
        this.end = (Date) end.clone();
    }
/**
 * for setting new repeat interval. it will set interval also for not repeated task, but it will not change anything
 * @param interval new interval
 */
    public void setInterval(int interval) {
        this.interval = interval;
    }

/**
 * return date which is next time after param, when task should be done. return null if task will not be done after
 * that date. if task is inactive - return false
 * @param current date, after which you looking for next time task should be done
 * @return date, when task should be done or null if task inactive or will not be done after param
 */
public Date nextTimeAfter(Date current) {
    int ninterval = this.interval*1000;
        if (!active) return null;
        if (!repeated) {
            if (current.after(time) || (current.equals(time))) {
                return null;
            } else return time;
        } else {
            if (current.after(end) || current.equals(end)) return null;
            if (current.before(start)) return start;
            long seconds = start.getTime();
            Date clone = new Date(seconds);
            if (current.equals(start)) return new Date(seconds + ninterval);
            while(clone.before(current) || clone.equals(current)) {
                clone.setTime(seconds);
                seconds = seconds + ninterval;
                clone.setTime(seconds);
            }
        return clone;
        }
    }

/**
 * compare task wirh object, return true if they are equal
 * @param o object, with which method compare task
 * @return true if they are equal or false if different
 */
    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (o.getClass() != Task.class) return false;
        Task task = (Task) o;
        if (this == task) return true;
        if (task == null) return false;
        if (repeated != task.repeated) return false;
         if (repeated) {
            if (!start.equals(task.start)) return false;
            if (!end.equals(task.end)) return false;
            if (interval != task.interval) return false;
        }
        if (!repeated) {
        if (!time.equals(task.time)) return false; }
        if (active != task.active) return false;
        return title.equals(task.title);
    }

/**
 * make hashcode of task
 * @return hashcode of task
 */
    @Override
    public int hashCode() {
        int result = title.hashCode();
        if (repeated) {
            result = 31 * result + start.hashCode();
            result = 31 * result + end.hashCode();
            result = 31 * result + interval;
        }
        else result = 31 * result + time.hashCode();

        result = 31 * result + (active ? 1 : 0);
        result = 31 * result + (repeated ? 1 : 0);
        return result;
    }

/**
 * make text, which describe task with all parameters of it
 * @return sting which describe task
 */
    @Override
    public String toString() {
        String rezult;
        if (repeated) {
            rezult = "{" + title + ": start=" + start + ", end=" + end + ", interval=" + interval/3600 + "h, repeated";
        } else rezult = "{" + title + " time=" + time + ", not repeated ";
        if(active) rezult+=", active}";
        else rezult+=", not active";
        return rezult;
    }

/**
 * make new task which is clone to that task
 * @return clone of task
 */
    @Override
    protected Task clone() {
       Task task;
        boolean active = this.active;
        boolean repeted = this.repeated;
        if (repeted) {
            String newTitle = new String(this.title);
            Date start = (Date) this.start.clone();
            Date end = (Date) this.end.clone();
            int interval = this.interval;
            task = new Task(newTitle, start, end, interval);
            task.setActive(active);
            task.repeated = true;
        } else {
            String newTitle = new String(this.title);
            Date time = (Date) this.time.clone();
            task = new Task(newTitle, time);
            task.setActive(active);
            task.repeated = false;
        }
        return task;
    }
}