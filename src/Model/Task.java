package Model;
import java.util.Date;

public class Task implements Cloneable {
    private String title = null;
    private Date time = null;
    private Date start = null;
    private Date end = null;
    private int interval;
    private boolean active;
    private boolean repeated;

    public Task() {}

    public Task(String title, Date time) {
        this.title = title;
        this.time = (Date) time.clone();
        repeated = false;
    }

    public Task(String title, Date start, Date end, int interval) {
        this.title = title;
        this.interval = interval;
        this.start = (Date) start.clone();
        this.end = (Date) end.clone();
        repeated = true;
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

    public Date getTime() {
        if (!repeated) {
            return time;
        } else {
            return start;
        }
    }

    public void setTime(Date time) {
        if (repeated) {
            repeated = false;
            this.time = (Date) time.clone();
        }
        else this.time = (Date) time.clone();
    }

    public Date getStartTime() {
        if (repeated) return start;
        else return time;
    }

    public Date getEndTime() {
        if (repeated) return end;
        else return time;
    }

    public int getRepeatInterval() {
        if (repeated) return interval;
        else return 0;
    }

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
    public void setRepeated(boolean repeated){
        this.repeated= repeated;
}
    public boolean isRepeated() {
        return  repeated;
    }
   
    public void setStart(Date start) {
        this.start = (Date) start.clone();
    }
   
    public void setEnd(Date end) {
        this.end = (Date) end.clone();
    }
    
    public void setInterval(int interval) {
        this.interval = interval;
    }
    
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