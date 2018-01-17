package Model;
import java.util.*;

public class Tasks {
         public static Iterable<Task> incoming(Iterable<Task> tasks, Date start, Date end) {
            Iterator<Task> iter = tasks.iterator();
                 ArrayTaskList list = new ArrayTaskList();
                 Task temp = null;
                 while (iter.hasNext()) {
                     temp = iter.next().clone();
                     System.out.println(temp);
                     if ((!(temp.nextTimeAfter(start) == null)) && (temp.nextTimeAfter(start).before(end) || temp.nextTimeAfter(start).equals(end))){
                         list.add(temp);
                     }
                 }
                 return list;
         }

       public static  SortedMap<Date, Set<Task>> calendar(Iterable<Task> tasks, Date start, Date end){
            SortedMap<Date, Set<Task>> calendar = new TreeMap<>();
           // HashSet<Task> list = (HashSet<Task>) incoming(tasks, start, end);
           ArrayTaskList list = (ArrayTaskList) incoming(tasks,start,end);
            Task temp;
            Iterator<Task> iterator = list.iterator();
            Date date;
            while (iterator.hasNext()) {
                temp = iterator.next();
                if (!temp.isRepeated()&&temp.isActive()) {
                    if (!temp.nextTimeAfter(start).after(end)) {
                        if (calendar.containsKey(temp.nextTimeAfter(start))) {
                            HashSet<Task> ts = new HashSet<>();
                            ts.add(temp);
                            calendar.put(temp.nextTimeAfter(start), ts);
                        } else {
                            calendar.get(temp.nextTimeAfter(start)).add(temp);
                        }
                    }
                } else if(temp.isActive()){
                    date = temp.nextTimeAfter(start);
                    while (!date.after(end)&&date!=null) {
                        if (calendar.containsKey(date)) {
                            calendar.get(date).add(temp);
                        }
                        else {
                            HashSet<Task> ts = new HashSet<>();
                            ts.add(temp);
                            calendar.put(date, ts);
                        }
                        System.out.println(temp.getRepeatInterval()+"-----");
                        date.setTime(date.getTime()+temp.getRepeatInterval()*1000*3600);
                      //  date = temp.nextTimeAfter(date);
                    }
                }
            }
           return calendar;
        }
}

