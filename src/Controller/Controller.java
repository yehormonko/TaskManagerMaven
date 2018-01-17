package Controller;

import Model.ArrayTaskList;
import Model.Task;
import Model.TaskIO;
import Model.Tasks;

import java.io.*;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.logging.Logger;

public class Controller {
	private static final Logger logger = Logger.getLogger(String.valueOf(Controller.class));
	private ArrayTaskList list = new ArrayTaskList();
	private File file = new File("Tasks");
	public Controller(){
		TaskIO.readBinary(list,file);
	}
	public void addTask(Task task) {
		list.add(task);
		TaskIO.writeBinary(list, file);
	}
	public void remove(Task task){
		list.remove(task);
		TaskIO.writeBinary(list,file);
	}
	public void rewrite(){
		TaskIO.writeBinary(list,file);
	}
	public void edit(Task task, int number){
		list.remove(list.getTask(number));
		list.add(task);
		TaskIO.writeBinary(list,file);
	}
	public String display(){
	String rezult="";
	for(int i=0;i<list.size();i++){
		//rezult+=i+1+". "+list.getTask(i).getTitle()+"\n";
		rezult+=i+1+". "+list.getTask(i)+"\n";
	}
	return rezult;
	}
	public String incoming(Date start, Date end){
		return Tasks.incoming(list,start,end).toString();
	}
	public ArrayTaskList getArrayTaskList(){
		return list;
	}
}
