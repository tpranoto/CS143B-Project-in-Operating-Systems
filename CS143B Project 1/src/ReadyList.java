//Timothy Pranoto
//3896411
//CS143B Project 1
//Processes and Resources Management

import java.util.*;

//ready list for priority 0 1 2
public class ReadyList {
	
	private ArrayList<LinkedList<PCB>> ready_list;
	
	public ReadyList(){
		ready_list = new ArrayList<LinkedList<PCB>>();
		for(int i=0;i<3;++i)
			ready_list.add(new LinkedList<PCB>());
	}
	
	public void add(PCB pcb){
		int priority = pcb.getPriority();
		ready_list.get(priority).add(pcb);
	}
	
	public void remove(PCB pcb){
		int priority = pcb.getPriority();
		ready_list.get(priority).remove(pcb);
	}
	
	public PCB getHighestPriorityPCB(){
		PCB highest = null;
		if(!ready_list.get(2).isEmpty()){
			highest = ready_list.get(2).getFirst();
		} else if(!ready_list.get(1).isEmpty()){
			highest = ready_list.get(1).getFirst();
		} else if(!ready_list.get(0).isEmpty()){
			highest = ready_list.get(0).getFirst();
		}
		return highest;
	}
	
	public PCB getPCBFromPid(String pid){
		for(int i = 2; i >= 0; i--){
			Iterator<PCB> it = ready_list.get(i).iterator();
			while(it.hasNext()){
				PCB pcb = it.next();
				if(pcb.getPid().equals(pid)){
					return pcb;
				}
			}
		}
		return null;
	}
}
