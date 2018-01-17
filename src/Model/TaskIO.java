
package Model;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;


public class TaskIO {
    public static void write(TaskList tasks, OutputStream out) {
        try (DataOutputStream writer = new DataOutputStream(out)) {
            writer.writeInt(tasks.size());
            for (Task task : tasks) {
                if (task != null) {
                    writer.writeInt(task.getTitle().length());
                    writer.writeUTF(task.getTitle());
                }
                if (task.isActive()) writer.writeInt(1);
                else writer.writeInt(0);
                writer.writeInt(task.getRepeatInterval());
                if (task.isRepeated()) {
                    writer.writeLong(task.getStartTime().getTime());
                    writer.writeLong(task.getEndTime().getTime());
                } else writer.writeLong(task.getTime().getTime());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void read(TaskList tasks, InputStream in) {
       try (DataInputStream reader = new DataInputStream(in)) {
           if (reader == null) reader.close();
           int size = 0;
           String title;
           Date time, start, end;
           int interval;
           boolean active;
           size = reader.readInt();
           for (int i = 0; i < size; i++) {
               reader.readInt();
               title = reader.readUTF();
               if (reader.readInt() == 0) active = false;
               else active = true;
               interval = reader.readInt();
               if (interval == 0) {
                   time = new Date(reader.readLong());
                   Task task = new Task(title, time);
                   task.setActive(active);
                   tasks.add(task);
               } else {
                   start = new Date(reader.readLong());
                   end = new Date(reader.readLong());
                   Task task = new Task(title, start, end, interval);
                   task.setActive(active);
                   tasks.add(task);
               }
           }
       } catch (IOException e) {
           e.printStackTrace();
       }
    }

    public static void writeBinary(TaskList tasks, File file) {
        try {
            write(tasks, new FileOutputStream(file.getPath()));
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public static void readBinary(TaskList tasks, File file) {
       try {
           if (!file.exists())
               writeBinary(new ArrayTaskList(), file);
               read(tasks, new FileInputStream(file.getPath()));
       } catch (IOException e) {
           e.printStackTrace();
       }
    }


    public static void write(TaskList tasks, Writer out) {
       try (BufferedWriter writer = new BufferedWriter(out)) {
           String ts = "";
           SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.ENGLISH);
           int counter = tasks.size();
           Task task;
           for (int i = 0; i < tasks.size(); i++) {
               task = tasks.getTask(i);

               ts = "\"" + task.getTitle() + "\"" + " ";
               if (task.isRepeated()) {
                   ts += "from " + "[" + format.format(task.getStartTime()) + "]" + " to [" + format.format(task.getEndTime()) + "]" + getrepeat(task.getRepeatInterval());
               } else {
                   ts += "at [" + format.format(task.getTime()) + "]";
               }
               if (!task.isActive()) ts += " inactive";
               counter--;
               if (counter >= 1) ts += ";\n";
               else ts += ".\n";
               writer.write(ts);
           }
       } catch (IOException e) {
           e.printStackTrace();
       }
    }

    public static void read(TaskList tasks, Reader in) {
        try (BufferedReader reader = new BufferedReader(in)) {
            if (reader == null) reader.close();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.ENGLISH);
            String task = "";
            char[] mass;
            String title;
            Date time = null;
            Date start = null;
            Date end = null;
            int interval = 0;
            boolean active = true;
            boolean repeated = false;
            while (reader.ready()) {
                task = reader.readLine();
                mass = task.toCharArray();
                int startStr = 1;
                int endStr = 0;
                for (int i = 0; i < mass.length; i++) {
                    if (mass[i] == '\"') endStr = i;
                }
                title = task.substring(1, endStr);
                for (int i = endStr; i < mass.length; i++) {
                    if (mass[i] == 'a' && mass[i + 1] == 't') repeated = false;
                    if (mass[i] == 'f' && mass[i + 1] == 'r') repeated = true;
                }
                if (!repeated) {
                    for (int i = endStr; i < mass.length; i++) {
                        if (mass[i] == '[') startStr = i + 1;
                        if (mass[i] == ']') {
                            endStr = i;
                        }
                    }
                    String date = task.substring(startStr, endStr);
                   try {
                       time = format.parse(date);
                   } catch (ParseException p){
                       p.printStackTrace();
                   }
                } else {
                    boolean foundst = false;
                    for (int i = endStr; i < mass.length; i++) {
                        if (mass[i] == '[') {
                            startStr = i + 1;
                            foundst = true;
                        }
                        if (mass[i] == ']' && foundst) {
                            endStr = i - 1;
                            break;
                        }
                    }
                    String date = task.substring(startStr, endStr + 1);
                    try {
                        start = format.parse(date);
                    } catch (ParseException p){
                        p.printStackTrace();
                    }
                    endStr += 2;
                    foundst = false;
                    for (int i = endStr; i < mass.length; i++) {
                        if (mass[i] == '[') {
                            startStr = i + 1;
                            foundst = true;
                        }
                        if (mass[i] == ']' && foundst) {
                            endStr = i - 1;
                            break;
                        }
                    }
                    String date2 = task.substring(startStr, endStr + 1);
                    try {
                        end = format.parse(date2);
                    } catch (ParseException p){
                        p.printStackTrace();
                    }
                    endStr += 2;
                    for (int i = endStr; i < mass.length; i++) {
                        if (mass[i] == 'd' && mass[i + 1] == 'a') {
                            if (Character.isDigit(mass[i - 3]))
                                interval += Character.getNumericValue(mass[i - 3]) * 10 * 86400;
                            interval += Character.getNumericValue(mass[i - 2]) * 86400;
                        }
                        if (mass[i] == 'h' && mass[i + 1] == 'o') {
                            if (Character.isDigit(mass[i - 3]))
                                interval += Character.getNumericValue(mass[i - 3]) * 10 * 3600;
                            interval += Character.getNumericValue(mass[i - 2]) * 3600;
                        }
                        if (mass[i] == 'm' && mass[i + 1] == 'i') {
                            if (Character.isDigit(mass[i - 3]))
                                interval += Character.getNumericValue(mass[i - 3]) * 10 * 60;
                            interval += Character.getNumericValue(mass[i - 2]) * 60;
                        }
                        if (mass[i] == 's' && mass[i + 1] == 'e') {
                            if (Character.isDigit(mass[i - 3])) interval += Character.getNumericValue(mass[i - 3]) * 10;
                            interval += Character.getNumericValue(mass[i - 2]);
                        }
                    }
                }
                if (endStr + 1 < task.length()) {
                    for (int i = endStr; i < mass.length; i++) {
                        if (mass[i] == 'i' && mass[i + 1] == 'n' && mass[i + 2] == 'a') active = false;
                    }
                }
                if (repeated) {
                    Task task1 = new Task(title, start, end, interval);
                    task1.setActive(active);
                    tasks.add(task1);
                } else {
                    Task task1 = new Task(title, time);
                    task1.setActive(active);
                    tasks.add(task1);
                }
                active = true;
                interval = 0;
            }
            if (tasks.getClass() == LinkedTaskList.class) {
                LinkedTaskList.ref((LinkedTaskList) tasks);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeText(TaskList tasks, File file) {
       try {
           write(tasks, new FileWriter(file));
       } catch (IOException e) {
           e.printStackTrace();
       }
    }
    public static void readText(TaskList tasks, File file) {
        try {
            read(tasks, new FileReader(file.getPath()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getrepeat(int repeat) {
        String ret = "every [";
        int days = repeat / 86400;
        int hours = (repeat % 86400) / 3600;
        int minutes = ((repeat % 86400) % 3600) / 60;
        int seconds = (((repeat % 86400) % 3600) % 60);
        if (days == 1) ret += days + " day ";
        if (days > 1) ret += days + " days ";
        if (hours == 1) ret += hours + " hour ";
        if (hours > 1) ret += hours + " hours ";
        if (minutes == 1) ret += minutes + " minute ";
        if (minutes > 1) ret += minutes + " minutes ";
        if (seconds == 1) ret += seconds + " second";
        if (seconds > 1) ret += seconds + " seconds";
        ret += "]";
        return ret;
    }
}


