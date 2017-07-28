//Timothy Pranoto
//3896411
//CS143B Project 1
//Processes and Resources Management

import java.util.*;

public class Manager {

	private PCB currentProcess;
	private LinkedList<RCB> resources;
	private ReadyList ready_list;
	
	private PCB getPCB(String pid){
		PCB pcb = null;
		if(currentProcess.getPid().equals(pid))
			pcb=currentProcess;
		else {
			pcb = ready_list.getPCBFromPid(pid);
		}
		if(pcb == null){
			for(RCB rcb : this.resources){
				Iterator<PCB> it = rcb.getWaiting_list().iterator();
				while(it.hasNext()){
					PCB tmp_pcb = it.next();
					if(tmp_pcb.getPid().equals(pid)){
						pcb = tmp_pcb;
					}else{
						pcb = null;
					}
				}
			}
		}
		return pcb;
	}
	private RCB getRCB(String rid){
		Iterator<RCB> it = this.resources.iterator();
		while (it.hasNext()) {
			RCB rcb = it.next();
			if (rcb.getRid().equals(rid))
				return rcb;
		}
		return null;	
	}

	public Manager(){
		currentProcess = new PCB();
		resources = new LinkedList<RCB>();
		resources.add(new RCB("R1",1));
		resources.add(new RCB("R2",2));
		resources.add(new RCB("R3",3));
		resources.add(new RCB("R4",4));
		ready_list = new ReadyList();
	}
	
	public boolean createProcess(String pid, int priority){
		if(priority==0)
			return false;
		if(getPCB(pid)!=null)
			return false;
		PCB pcb = new PCB(pid, priority);
		currentProcess.getCreationTree().children.add(pcb);
		pcb.getCreationTree().parent = currentProcess;
		ready_list.add(pcb);
		scheduler();
		return true;
	}
	
	public boolean destroyProcess(String pid){
		PCB pcb = getPCB(pid);
		if(pcb==null||pid.equalsIgnoreCase("init"))
			return false;
		kill_Tree(pcb);
		scheduler();
		return true;
	}
	
	public void kill_Tree(PCB pcb){
		for (PCB child:pcb.getCreationTree().children)
			kill_Tree(child);
		pcb.getCreationTree().children.clear();
		pcb.getCreationTree().parent.getCreationTree().children.remove(pcb);
		for(RCB rcb:pcb.getResourceList())
			releaseResource(rcb.getRid(),rcb.getStatus().getMax());
		if (currentProcess != null && currentProcess.equals(pcb))
			currentProcess = null;
		else
			ready_list.remove(pcb);			
	}
	
	public boolean requestResource(String rid, int n){
		RCB rcb = getRCB(rid);
		if(n <=0)
			return false;
		if(rcb.getStatus().getMax()<n)
			return false;
		if(currentProcess.getPid().equalsIgnoreCase("init"))
			return false;
		if(currentProcess.getResourceList().contains(rcb))
			if(currentProcess.getResourceList().get(currentProcess.getResourceList()
					.indexOf(rcb)).getStatus().getUsed()+n>rcb.getStatus().getMax())
				return false;
		if(rcb.getStatus().getAvailable()>=n){
			rcb.getStatus().takeResource(n);
			currentProcess.getResourceList().add(rcb);
		}
		else{
			currentProcess.getStatus().setStatusBLOCKED();
			ready_list.remove(currentProcess);
			rcb.getWaiting_list().add(currentProcess);
			scheduler();
		}
		return true;
	}
	
	public boolean releaseResource(String rid, int n){
		RCB rcb = getRCB(rid);
		boolean checkRel = false;
		if(!currentProcess.getResourceList().remove(rcb))
			checkRel = true;
		if(n<=0)
			return false;
		if(rcb.getStatus().getAvailable()==rcb.getStatus().getMax())
			return false;
		if(rcb.getWaiting_list().isEmpty()){
			if(n>rcb.getStatus().getMax())
				return false;
			rcb.getStatus().giveResource(n);
		}
		else{
			PCB pcb = rcb.getWaiting_list().removeFirst();
			rcb.getWaiting_list().remove(pcb);
			pcb.getStatus().setStatusREADY();
			pcb.getResourceList().add(rcb);
			ready_list.add(pcb);
			scheduler();
		}
		if(checkRel)
			return false;
		return true;
	}
	
	public void scheduler(){
		PCB pcb = ready_list.getHighestPriorityPCB();
		if (currentProcess == null || 
				currentProcess.getPriority() < pcb.getPriority() || 
				currentProcess.getStatus().getType() != PCB.statusType.RUNNING){
			if (currentProcess != null
					&& currentProcess.getStatus().getType() == PCB.statusType.RUNNING){
				currentProcess.getStatus().setStatusREADY();
				ready_list.add(currentProcess);
			}
			pcb.getStatus().setStatusRUNNING();
			currentProcess = pcb;
			ready_list.remove(pcb);
		}	
	}
	
	public void time_out(){
		ready_list.add(currentProcess);
		currentProcess.getStatus().setStatusREADY();
		scheduler();
	}
	
	public String getCurrentProcess(){
		return currentProcess.toString();
	}
}
