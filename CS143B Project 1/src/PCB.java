//Timothy Pranoto
//3896411
//CS143B Project 1
//Processes and Resources Management

import java.util.*;

//Process Control Block
public class PCB {
	
	public enum statusType {
		READY, RUNNING, BLOCKED
	}
	public class ProcessStatus {
		// Status for each PCB filled with type and list
		private statusType type;
		private ReadyList ready_list;

		public ProcessStatus() {
			type = statusType.READY;
			ready_list = new ReadyList();
		}
		
		public ReadyList getRL(){
			return ready_list;
		}
		public statusType getType(){
			return type;
		}
		public void setStatusREADY(){
			type = statusType.READY;
		}
		public void setStatusRUNNING(){
			type = statusType.RUNNING;
		}
		public void setStatusBLOCKED(){
			type = statusType.BLOCKED;
		}
	}

	public class CreationTree{
		PCB parent;
		LinkedList<PCB> children;
		public CreationTree(){
			parent = null;
			children = new LinkedList<PCB>();
		}
	}
	
	private String pid;
	private ProcessStatus status;
	private CreationTree creation_tree;
	private int priority;
	private LinkedList<RCB> resources_list;
	
	//constructor for init
	public PCB(){
		pid = "init";
		status = new ProcessStatus();
		status.setStatusRUNNING();
		creation_tree = new CreationTree();
		priority = 0;
		resources_list = new LinkedList<RCB>();
		
	}
	
	public PCB(String pidInput, int prioInput){
		pid = pidInput;
		status = new ProcessStatus();
		creation_tree = new CreationTree();
		priority = prioInput;
		resources_list = new LinkedList<RCB>();
	}
	
	public String getPid(){
		return pid;
	}
	public ProcessStatus getStatus(){
		return status;
	}
	public CreationTree getCreationTree(){
		return creation_tree;
	}
	public int getPriority(){
		return priority;
	}
	public LinkedList<RCB> getResourceList(){
		return resources_list;
	}
	public String toString(){
		return pid +" ";
	}
}
