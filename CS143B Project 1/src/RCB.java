//Timothy Pranoto
//3896411
//CS143B Project 1
//Processes and Resources Management

import java.util.*;

//Resource Control Block
public class RCB {
	public class Status{
		private int max;
		private int available;
		public Status(int maxR){
			max=maxR;
			available = maxR;
		}
		public int getMax(){
			return max;
		}
		public int getAvailable(){
			return available;
		}
		public int getUsed(){
			return (max-available);
		}
		public void takeResource(int n){
			available -=n;
		}
		public void giveResource(int n){
			available +=n;
		}
	}
	
	private String rid;
	private Status status;
	private LinkedList<PCB> waiting_list;
	
	public RCB(String rid, int n){
		this.rid = rid;
		this.status = new Status(n);
		this.waiting_list = new LinkedList<PCB>();
		
	}
	
	public String getRid(){
		return rid;
	}
	public Status getStatus(){
		return status;
	}
	public LinkedList<PCB> getWaiting_list(){
		return waiting_list;
	}
}
