import Model.ArrayTaskList;
import Model.Task;
import Model.TaskIO;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Scanner;

public class ViewController {
public static void main(String[] args) {
	ViewController app = new ViewController();
	app.menu();
}
private static final Logger logger = Logger.getLogger(ViewController.class);

private ArrayTaskList list = new ArrayTaskList();
private File file;
private void loadFile(){
	System.out.println("Here list of files you can load. Choose one or create another");
	File directory = new File("TasksFiles");
	if(!directory.exists()){
		directory.mkdir();
	}
	FilenameFilter filenameFilter = new FilenameFilter() {
		@Override
		public boolean accept(File dir, String name) {
			return name.endsWith(".txt");
		}
	};
	String list[] = directory.list(filenameFilter);
	int i;
	for(i = 0; i < list.length; i++)
		System.out.println(i+1+". "+list[i]);
	System.out.println(i+1+". "+ "create new");
	Scanner scanner = new Scanner(System.in);
	int choose;
	while(true){
		choose=input();
		if(choose<1||choose>i+1){
			System.out.println("print number from 1 to "+(i+1));
		}
		else{
			break;
		}
	}
	if(choose==i+1){
		System.out.println("print name of new file");
		file = new File("TasksFiles",scanner.next()+".txt");
		try {
			file.createNewFile();
		} catch (IOException e) {
			logger.error(e);
			loadFile();
			return;
		}
	}
	else{
		file=new File("TasksFiles\\"+list[choose-1]);
		TaskIO.readText(this.list,file);
		}

	
}
public void remove(Task task){
	list.remove(task);
	TaskIO.writeBinary(list,file);
}
public void rewrite(){
	file.delete();
	try {
		file.createNewFile();
	} catch (IOException e) {
		logger.error(e);
	}
	TaskIO.writeText(list,file);
}
public ViewController(){
	System.out.println("Welcome to TASK MANAGER");
	loadFile();
}
public void writeMenu(){
	System.out.println("________________________________\n" +
			"Choose action:\n" +
			"1. Print tasks\n" +
			"2. Add task\n" +
			"3. Remove task\n" +
			"4. Show tasks from date to date\n" +
			"5. Edit task\n"+
			"6. EXIT\nor type \"exit\" to exit\n_________________________________   ");
}
public void menu() {
	writeMenu();
	Scanner scanner = new Scanner(System.in);
	int choose = 0;
	if (scanner.hasNextInt()) {
		choose = scanner.nextInt();
	} else {
		String exit=scanner.next();
		if(exit.toLowerCase().equals("exit")){
			return;
		}else{
			System.out.println("print correct number or \"exit\"");
		}
		this.menu();
		return;
	}
	if (choose < -1 || choose > 6) {
		System.out.println("print correct number or \"exit\"");
		this.menu();
		return;
	}
	boolean stop = false;
	switch (choose) {
		case 1: {
			printTask();
			menu();
			break;
		}
		case 2: {
			while (!stop) {
				stop = addTask();
			}
			stop = false;
			menu();
				break;
			}
		
		case 3: {
			while (!stop) {
				stop = remove();
			}
			stop = false;
			menu();
			break;
		}
		case 4: {
			while (!stop) {
			stop = printFromTo();
			}
			menu();
			break;
		}
		case 5:{
			while (!stop) {
			stop = change();
			}
			menu();
			break;
		}
		case 6: {
			System.out.println("Are you sure? y/n");
			if (ask())
				return;
			else  {
				menu();
				return;
			}
		}
	}
}

public void printTask() {
	if(list.size()==0) {
		System.out.println("no tasks");
		return;
	}
	for (int i = 0; i < list.size(); i++) {
		System.out.println(i + 1 + ". " + list.getTask(i).toString());
	}
}

public boolean addTask() {
	String title;
	Date time, start, end;
	boolean repeated, active;
	Scanner scanner = new Scanner(System.in);
	System.out.println("print name");
	title = new Scanner(System.in).nextLine();
	System.out.println("Is it repeated? y/n");
	boolean choise = this.ask();
	Task created;
	if (!choise) {
		repeated = false;
		created = new Task(title, this.inputDate());
	} else {
		repeated = true;
		String date, tm;
		System.out.println("START");
		start = this.inputDate();
		System.out.println("END");
		end = this.inputDate();
		if (start.after(end)||start.equals(end)){
			System.out.println("date START is before END or they are equal");
			return false;
		}
		System.out.println("print interval in hours");
		int interval = input()*3600;
		if(new Date(start.getTime()+interval).after(end)) {
			System.out.println("with this interval task is impossible");
			return false;
		}
		created = new Task(title, start, end, interval);
	}
	System.out.println("would you like make it active? y/n");
	choise = ask();
	if (choise) created.setActive(true);
	else created.setActive(false);
	list.add(created);
	rewrite();
	return true;
}

public boolean remove() {
	this.printTask();
	int choise;
	System.out.println("Which you want to delete? 0 if no one");
	Scanner scanner = new Scanner(System.in);
	if (!scanner.hasNextInt()) {
		System.out.println("print correct number");
		return false;
	}
	choise = scanner.nextInt();
	if(choise==0) return true;
	if (choise < 1 || choise > list.size()) {
		System.out.println("print correct number");
		return false;
	}
	
	remove(list.getTask(choise - 1));
	return true;
}

private Date inputDate() {
	String date=null, tm=null;
	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SS", Locale.ENGLISH);
	Scanner scanner = new Scanner(System.in);
	System.out.println("print date in format yyyy-MM-dd");
	date = scanner.next();
	Date newDate;
	try{
		newDate=format.parse(date+" 00:00:00.00");
	}
	catch (ParseException e){
		System.out.println("Wrong date format");
		return this.inputDate();
	}
	System.out.println("print time in format HH:mm");
	tm = " " + scanner.next() + ":00.00";
	try {
		newDate = format.parse(date + tm);
	} catch (ParseException e) {
		System.out.println("Wrong date format");
		return this.inputDate();
	}
	return newDate;
}

public boolean printFromTo() {
	ArrayTaskList list;
	System.out.println("FROM");
	Date start = this.inputDate();
	System.out.println("TO");
	Date end = this.inputDate();
	if(start.after(end)) {
		System.out.println("date FROM is after date TO");
		return false;
	}
	list = (ArrayTaskList) this.list.incoming(start, end);
	if(list.size()==0){
		System.out.println("no tasks in this period");
		return true;
	}
	for (int i = 0; i < list.size(); i++) {
		System.out.println(i + 1 + ". " + list.getTask(i).toString());
	}
	return true;
}


private boolean ask() {
	Scanner scanner = new Scanner(System.in);
	char choose = scanner.next().charAt(0);
	if (choose == 'y') return true;
	if (choose == 'n') return false;
	System.out.println("\"y\" or \"n\"");
	return this.ask();
}

private int input() {
	Scanner scanner = new Scanner(System.in);
	if (scanner.hasNextInt()) {
		return scanner.nextInt();
	} else {
		this.input();
		System.out.println("print number");
	}
	return 0;
}
public void writeChangeMenu(){
	System.out.println("what you want to change:" +
			"\n1. Name" +
			"\n2. Activate or deactivate" +
			"\n3. Make repeated or not repeated" +
			"\n4. Change date(s)" +
			"\n5. Change interval if its repeated" +
			"\n6. Return to list of task" +
			"\n7. Exit");
}
public boolean change() {
	this.printTask();
	System.out.println("which do you want to change? if no one - print 0");
	int number = input();
	if (number<1||number>list.size()) {
		System.out.println("print number from 0 to " + list.size());
		return false;
	} else {
		boolean again = false;
		if (number == 0) return true;
		Task redacted = list.getTask(number-1);
		while (!again) {
			System.out.println("you choosed " +redacted);
			writeChangeMenu();
			int check = input();
			if(check<1||check>7){
				System.out.println("print correct number");
				again=false;
			}
			switch (check) {
				
				case 1: {
					System.out.println("input new name");
					String newName = new Scanner(System.in).nextLine();
					redacted.setTitle(newName);
					rewrite();
					System.out.println("would you like to change something else? y/n");
					again = !ask();
					break;
				}
				case 2: {
					System.out.println("now its activity: " + redacted.isActive());
					System.out.println("make it active(y) or not active(n)?");
					boolean act = ask();
					redacted.setActive(act);
					rewrite();
					System.out.println("would you like to change something else? y/n");
					again = !ask();
					break;
				}
				case 3:{
					boolean rep=redacted.isRepeated();
					System.out.println("repeated: "+rep);
					System.out.println("do you want to change it? y/n");
					boolean cont = ask();
					if(rep&&cont){
						System.out.println("input TIME");
						Date newTime = inputDate();
						redacted.setRepeated(false);
						redacted.setTime(newTime);
						rewrite();
					}
					if(!rep&&cont){
						System.out.println("input START");
						Date newStart = inputDate();
						System.out.println("input END");
						Date newEnd = inputDate();
						if (newStart.after(newEnd)||newStart.equals(newEnd)){
							System.out.println("date START is before END or they are equal");
							again=false;
							break;
						}
						System.out.println("print interval");
						int newInterval= input();
						if(new Date(newStart.getTime()+newInterval).after(newEnd)){
							System.out.println("with this interval task is impossible");
							again=false;
							break;
						}
						redacted.setRepeated(true);
						redacted.setTime(newStart,newEnd,newInterval);
						rewrite();
					}
					System.out.println("would you like to change something else? y/n");
					again = !ask();
					break;
				}
				case 4:{
					boolean rep=redacted.isRepeated();
					if(!rep){
						System.out.println("input TIME");
						Date newTime = inputDate();
						redacted.setTime(newTime);
						rewrite();
					}
					if(rep){
						System.out.println("input FROM");
						Date newStart = inputDate();
						System.out.println("input TO");
						Date newEnd = inputDate();
						redacted.setTime(newStart,newEnd,redacted.getRepeatInterval());
						rewrite();
					}
					System.out.println("would you like to change something else? y/n");
					again = !ask();
					break;
				}
				case 5:{
					boolean rep = redacted.isRepeated();
					if(!rep){
						System.out.println("it is not repeated");
						again=false;
						this.change();
						return true;
					}else{
						System.out.println("print interval in hours");
						int newInterval= input();
						if(new Date(redacted.getStartTime().getTime()+newInterval).after(redacted.getEndTime())){
							System.out.println("with this interval task is impossible");
							again=false;
							break;
						}
						redacted.setInterval(newInterval*3600);
						rewrite();
						System.out.println("would you like to change something else? y/n");
						again = !ask();
						break;
					}
				}
				case 6:{
					return false;
				}
				case 7:{
					return true;
				}
			}
			
		}
	}
	return true;
}
}
