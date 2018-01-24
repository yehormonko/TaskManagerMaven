//package View;
//
//import Controller.Controller;
//import Model.ArrayTaskList;
//import Model.Task;
//import Model.Tasks;
//
//import org.apache.log4j.PropertyConfigurator;
//
//import java.io.PrintWriter;
//import java.io.StringWriter;
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.*;
//import java.util.logging.Logger;
//
//public class View {
//
//private Controller controller;
//
//public View(Controller controller) {
//	this.controller = controller;
//}
//
//public void menu() {
//	PropertyConfigurator.configure("log4j.properties");
//	org.apache.log4j.Logger logger = org.apache.log4j.Logger.getRootLogger();
//	StringWriter sw = new StringWriter();
//	PrintWriter pw = new PrintWriter(sw);
//	String sStackTrace;
//	System.out.println("________________________________\n" +
//			"Choose action:\n" +
//			"1. Print tasks\n" +
//			"2. Add task\n" +
//			"3. Remove task\n" +
//			"4. Show tasks frome date to date\n" +
//			"5. Edit task\n"+
//			"6. EXIT\n_________________________________   ");
//	Scanner scanner = new Scanner(System.in);
//	int choose = 0;
//	if (scanner.hasNextInt()) {
//		choose = scanner.nextInt();
//	} else {
//		this.menu();
//		return;
//	}
//	if (choose < -1 || choose > 6) {
//		this.menu();
//		return;
//	}
//	boolean stop = false;
//	switch (choose) {
//		case 1: {
//			try {
//				printTask();
//			}catch (Exception e){
//				e.printStackTrace(pw);
//				sStackTrace = sw.toString();
//				logger.warn(sStackTrace);
//				System.out.println("error");
//			}
//			System.out.println("Return to menu? y/n");
//			{
//				if (ask())
//					menu();
//				return;
//			}
//		}
//		case 2: {
//			while (!stop) {
//				try {
//				stop = addTask();
//			}catch (Exception e){
//				e.printStackTrace(pw);
//				sStackTrace = sw.toString();
//				logger.warn(sStackTrace);
//				System.out.println("error");
//			}
//			}
//			stop = false;
//			System.out.println("Return to menu? y/n");
//			{
//				if (ask())
//					menu();
//				return;
//			}
//		}
//		case 3: {
//			while (!stop) {
//				try {
//					stop = remove();
//				}catch (Exception e){
//					e.printStackTrace(pw);
//					sStackTrace = sw.toString();
//					logger.warn(sStackTrace);
//					System.out.println("error");
//				}
//				}
//			stop = false;
//			System.out.println("Return to menu? y/n");
//			{
//				if (ask())
//					menu();
//				return;
//			}
//		}
//		case 4: {
//			while (!stop) {
//				try {
//					stop = printFromTo();
//				}catch (Exception e){
//					e.printStackTrace(pw);
//					sStackTrace = sw.toString();
//					logger.warn(sStackTrace);
//					System.out.println("error");
//				}
//				//stop = printFromTo();
//			}
//			System.out.println("Return to menu? y/n");
//			{
//				if (ask())
//					menu();
//				return;
//			}
//		}
//		case 5:{
//			while (!stop) {
//				try {
//					stop = change();
//				}catch (Exception e){
//					e.printStackTrace(pw);
//					sStackTrace = sw.toString();
//					logger.warn(sStackTrace);
//					System.out.println("error");
//				}
//				//stop = change();
//			}
//			System.out.println("Return to menu? y/n");
//			{
//				if (ask())
//					menu();
//				return;
//			}
//		}
//		case 6: {
//			System.out.println("Are you sure? y/n");
//			if (ask())
//				return;
//			else  {
//				menu();
//				return;
//			}
//		}
//	}
//}
//
//public void printTask() {
//	ArrayTaskList list = new ArrayTaskList();
//	list = controller.getArrayTaskList();
//	if(list.size()==0) {
//		System.out.println("no tasks");
//		return;
//	}
//	for (int i = 0; i < list.size(); i++) {
//		System.out.println(i + 1 + ". " + list.getTask(i).toString());
//	}
//}
//
//public boolean addTask() {
//	String title;
//	Date time, start, end;
//	boolean repeated, active;
//	Scanner scanner = new Scanner(System.in);
//	System.out.println("print name");
//	title = new Scanner(System.in).nextLine();
//	System.out.println("Is it repeated? y/n");
//	boolean choise = this.ask();
//	System.out.println(choise);
//	Task created;
//	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SS", Locale.ENGLISH);
//	if (!choise) {
//		repeated = false;
//		created = new Task(title, this.inputDate());
//	} else {
//		repeated = true;
//		String date, tm;
//		System.out.println("START");
//		start = this.inputDate();
//		System.out.println("END");
//		end = this.inputDate();
//		if (start.after(end)||start.equals(end)){
//			System.out.println("date START is before END or they are equal");
//			return false;
//		}
//		System.out.println("print interval in hours");
//		int interval = input()*3600;
//		if(new Date(start.getTime()+interval).after(end)) {
//			System.out.println("with this interval task is impossible");
//			return false;
//		}
//		created = new Task(title, start, end, interval);
//	}
//	System.out.println("would you like make it active? y/n");
//	choise = ask();
//	if (choise) created.setActive(true);
//	else created.setActive(false);
//	controller.addTask(created);
//	return true;
//}
//
//public boolean remove() {
//	this.printTask();
//	int choise;
//	System.out.println("Which you want to delete? 0 if no one");
//	Scanner scanner = new Scanner(System.in);
//	if (!scanner.hasNextInt()) {
//		System.out.println("print correct number");
//		return false;
//	}
//	choise = scanner.nextInt();
//	if(choise==0) return true;
//	if (choise < 1 || choise > controller.getArrayTaskList().size()) {
//		System.out.println("print correct number");
//		return false;
//	}
//
//	controller.remove(controller.getArrayTaskList().getTask(choise - 1));
//	return true;
//}
//
//private Date inputDate() {
//	PropertyConfigurator.configure("log4j.properties");
//	org.apache.log4j.Logger logger = org.apache.log4j.Logger.getRootLogger();
//	StringWriter sw = new StringWriter();
//	PrintWriter pw = new PrintWriter(sw);
//	String sStackTrace;
//	String date, tm;
//	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SS", Locale.ENGLISH);
//	Scanner scanner = new Scanner(System.in);
//	System.out.println("print date in format yyyy-MM-dd");
//	date = scanner.next();
//	Date newDate;
//	System.out.println("print time in format HH:mm");
//	tm = " " + scanner.next() + ":00.00";
//	try {
//		newDate = format.parse(date + tm);
//	} catch (ParseException e) {
//		System.out.println("Wrong date format");
//		e.printStackTrace(pw);
//		sStackTrace = sw.toString();
//		logger.error(sStackTrace+"not parsed date: "+date+tm);
//		return this.inputDate();
//	}
//	return newDate;
//}
//
//public boolean printFromTo() {
//	ArrayTaskList list = new ArrayTaskList();
//	System.out.println("FROM");
//	Date start = this.inputDate();
//	System.out.println("TO");
//	Date end = this.inputDate();
//	if(start.after(end)||start.equals(end)) {
//		System.out.println("date FROM is before date TO or they are equal");
//		return false;
//	}
//	list = (ArrayTaskList) controller.getArrayTaskList().incoming(start, end);
//	if(list.size()==0){
//		System.out.println("no tasks in this period");
//		return true;
//	}
//	for (int i = 0; i < list.size(); i++) {
//		System.out.println(i + 1 + ". " + list.getTask(i).toString());
//	}
//	return true;
//}
//
////public void calend() {
////	System.out.println(controller.getArrayTaskList().getTask(6).getStartTime() +" \n"+controller.getArrayTaskList().getTask(6).getRepeatInterval()+"\n" +controller.getArrayTaskList().getTask(6).nextTimeAfter(controller.getArrayTaskList().getTask(6).getStartTime()));
////	System.out.println("FROM");
////	Date start = this.inputDate();
////	//Date start = new Date(0);
////	System.out.println("TO");
////	Date end = this.inputDate();
////	//Date end = new Date(100000);
////	System.out.println("+");
////	SortedMap<Date, Set<Task>> map = Tasks.calendar(controller.getArrayTaskList(), start, end);
////	System.out.println(map);
////	for (Map.Entry entry : map.entrySet()) {
////		if (map.isEmpty()) {
////			System.out.println("Empty");
////			return;
////		}
////		System.out.println(entry.getKey());
////		HashSet<Task> tasks = (HashSet<Task>) entry.getValue();
////		for (Task task : tasks) {
////			System.out.println("    " + task.toString());
////		}
////	}
////}
//
//private boolean ask() {
//	Scanner scanner = new Scanner(System.in);
//	char choose = scanner.next().charAt(0);
//	if (choose == 'y') return true;
//	if (choose == 'n') return false;
//	System.out.println("\"y\" or \"n\"");
//	return this.ask();
//}
//
//private int input() {
//	Scanner scanner = new Scanner(System.in);
//	if (scanner.hasNextInt()) {
//		return scanner.nextInt();
//	} else {
//		this.input();
//		System.out.println("print number");
//	}
//	return 0;
//}
//
//public boolean change() {
//	this.printTask();
//	System.out.println("which do you want to change? if no one - print 0");
//	Scanner scanner = new Scanner(System.in);
//	if (!scanner.hasNextInt()) {
//		System.out.println("print number from 0 to " + controller.getArrayTaskList().size());
//		return false;
//	} else {
//		boolean again = false;
//		int number = scanner.nextInt();
//		if (number == 0) return true;
//		Task redacted = controller.getArrayTaskList().getTask(number-1);
//		while (!again) {
//			System.out.println("you choosed " +redacted);
//			System.out.println("what you want to change:" +
//					"\n1. Name" +
//					"\n2. Activate or deactivate" +
//					"\n3. Make repeated or not repeated" +
//					"\n4. Change date(s)" +
//					"\n5. Change interval if its repeated" +
//					"\n6. Return to list of task" +
//					"\n7. Exit");
//
//			int check = input();
//			switch (check) {
//				case 1: {
//					System.out.println("input new name");
//					String newName = new Scanner(System.in).nextLine();
//					redacted.setTitle(newName);
//					controller.rewrite();
//					System.out.println("would you like to change something else? y/n");
//					again = !ask();
//					break;
//				}
//				case 2: {
//					System.out.println("now its activity: " + redacted.isActive());
//					System.out.println("make it active(y) or not active(n)?");
//					boolean act = ask();
//					redacted.setActive(act);
//					controller.rewrite();
//					System.out.println("would you like to change something else? y/n");
//					again = !ask();
//					break;
//				}
//				case 3:{
//					boolean rep=redacted.isRepeated();
//					System.out.println("repeated: "+rep);
//					System.out.println("do you want to change it? y/n");
//					boolean cont = ask();
//					if(rep&&cont){
//						System.out.println("input TIME");
//						Date newTime = inputDate();
//						redacted.setRepeated(false);
//						redacted.setTime(newTime);
//						controller.rewrite();
//					}
//					if(!rep&&cont){
//						System.out.println("input START");
//						Date newStart = inputDate();
//						System.out.println("input END");
//						Date newEnd = inputDate();
//						if (newStart.after(newEnd)||newStart.equals(newEnd)){
//							System.out.println("date START is before END or they are equal");
//							again=false;
//							break;
//						}
//						System.out.println("print interval");
//						int newInterval= input();
//						if(new Date(newStart.getTime()+newInterval).after(newEnd)){
//							System.out.println("with this interval task is impossible");
//							again=false;
//							break;
//						}
//						redacted.setRepeated(true);
//						redacted.setTime(newStart,newEnd,newInterval);
//						controller.rewrite();
//					}
//					System.out.println("would you like to change something else? y/n");
//					again = !ask();
//					break;
//				}
//				case 4:{
//					boolean rep=redacted.isRepeated();
//					if(!rep){
//						System.out.println("input TIME");
//						Date newTime = inputDate();
//						redacted.setTime(newTime);
//						controller.rewrite();
//					}
//					if(rep){
//						System.out.println("input FROM");
//						Date newStart = inputDate();
//						System.out.println("input TO");
//						Date newEnd = inputDate();
//						redacted.setTime(newStart,newEnd,redacted.getRepeatInterval());
//						controller.rewrite();
//					}
//					System.out.println("would you like to change something else? y/n");
//					again = !ask();
//					break;
//				}
//				case 5:{
//					boolean rep = redacted.isRepeated();
//					if(!rep){
//						System.out.println("it is not repeated");
//						again=false;
//						this.change();
//						return true;
//					}else{
//						System.out.println("print interval in hours");
//						int newInterval= input();
//						if(new Date(redacted.getStartTime().getTime()+newInterval).after(redacted.getEndTime())){
//							System.out.println("with this interval task is impossible");
//							again=false;
//							break;
//						}
//						redacted.setInterval(newInterval);
//						controller.rewrite();
//						System.out.println("would you like to change something else? y/n");
//						again = !ask();
//						break;
//					}
//				}
//				case 6:{
//					return false;
//				}
//				case 7:{
//					return true;
//				}
//			}
//
//		}
//	}
//	return true;
//}
//}
