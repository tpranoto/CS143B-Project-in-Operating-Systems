//Timothy Pranoto
//38964311
//CS143B Project 2

import java.text.*;
import java.util.*;

public class Scheduler {
	private int localTime;
	private int localIndex;
	ArrayList<Process> processes;
	
	public class Helper implements Comparator<Process>{
		public int compare(Process p1, Process p2) {
			if(p1.pid() < p2.pid())
				return -1;
			else if(p1.timeLeft() > p2.timeLeft())
				return 1;
			else if(p1.timeLeft() < p2.timeLeft())
				return -1;
			else
				return 1;
		}
	}

	public Scheduler(){
		localTime=0;
		localIndex=0;
		processes = new ArrayList<Process>();
	}

	public Scheduler(ArrayList<Process> p){
		this.processes = p;
	}
	
	private String avgTurnAroundTime(){
		NumberFormat formatter = new DecimalFormat("#0.00");
		String result;
		int totalTurnaround = 0;
		for(int i=0; i<processes.size(); i++){
			totalTurnaround += processes.get(i).turnAroundTime();
		}
		double avg = (double)totalTurnaround/processes.size();
		result = formatter.format(avg);
		
		return result;
	}
	
	private String writeOutput(){
		String result="";
		result +=avgTurnAroundTime()+" ";
		for(int i=0; i<processes.size(); i++){
			if(i == processes.size() - 1)
				result+=processes.get(i).turnAroundTime();
			else
				result+=processes.get(i).turnAroundTime() + " ";
		}
		return result;
	}
	
	public void reset(){
		localTime=0;
		localIndex=0;
		for(int i=0; i<processes.size(); i++){
			processes.get(i).reset();
		}
	}
	
	public String FIFO(){
		LinkedList<Process> processList = new LinkedList<Process>();
		while(localIndex<processes.size()){
			while(localIndex<processes.size() && processes.get(localIndex).startTime() <= localTime){
				processList.add(processes.get(localIndex));
				localIndex++;
			}
			if(processList.isEmpty()){
				localTime++;
				continue;
			}
			while(!processList.isEmpty()){
				Process pTemp = processList.remove();
				localTime += pTemp.duration();
				pTemp.setTime(localTime);
				if(localIndex < processes.size()) break;
			}
			
		}
		String result = writeOutput();
		reset();
		return result;
	}
	
	public String SJF(){
		PriorityQueue<Process> processList = new PriorityQueue<Process>(processes.size(), new Helper());
		while(localIndex < processes.size()){
			while(localIndex<processes.size() && processes.get(localIndex).startTime() <= localTime){
				processList.add(processes.get(localIndex));
				localIndex++;
			}
			if(processList.isEmpty()){
				localTime++;
				continue;
			}
			while(!processList.isEmpty()){
				Process pTemp = processList.remove();
				localTime += pTemp.duration();
				pTemp.setTime(localTime);
				if(localIndex < processes.size()) break;
			}
		}
		String result = writeOutput();
		reset();
		return result;
	}
	
	public String SRT(){
		PriorityQueue<Process> processList = new PriorityQueue<Process>(processes.size(), new Helper());
		while(localIndex < processes.size()){
			while(localIndex<processes.size() && processes.get(localIndex).startTime() <= localTime){
				processList.add(processes.get(localIndex));
				localIndex++;
			}
			if(processList.isEmpty()){
				localTime++;
				continue;
			}
			while(!processList.isEmpty()){
				Process pTemp = processList.remove();
				localTime++;
				pTemp.decrementTimeLeft();
				if(pTemp.isFinished()){
					pTemp.setTime(localTime);
				}else{
					processList.add(pTemp);
				}
				if(localIndex < processes.size()) break;
			}
		}
		String result = writeOutput();
		reset();
		return result;
	}
	
	public String MLF(){
		LinkedList<Process>[] processList =new LinkedList[5];
		for(int i=0;i<5;++i){
			processList[i]=new LinkedList<Process>();
		}
		while(localIndex < processes.size()){//check for true statement
			while(localIndex<processes.size() && processes.get(localIndex).startTime() <= localTime){
				processList[4].add(processes.get(localIndex));
				localIndex++;
			}
			if(processList[4].isEmpty()&&processList[3].isEmpty()&&processList[2].isEmpty()&&processList[1].isEmpty()&&processList[0].isEmpty()){
				localTime++;
				continue;
			}
			while(!(processList[4].isEmpty()&&processList[3].isEmpty()&&processList[2].isEmpty()&&processList[1].isEmpty()&&processList[0].isEmpty())){
				if(!processList[4].isEmpty()){
					Process pTemp = processList[4].remove();
					localTime++;
					pTemp.decrementTimeLeft();
					if(pTemp.isFinished()){
						pTemp.setTime(localTime);
					}else{
						processList[3].add(pTemp);
					}
					
				}
				else if(!processList[3].isEmpty()){
					Process pTemp = processList[3].remove();
					localTime++;
					pTemp.decrementTimeLeft();
					if(pTemp.isFinished()){
						pTemp.setTime(localTime);
					}else{
						if(pTemp.MLFHelper()<1){
							pTemp.incrementMLFHelper();
							processList[3].addFirst(pTemp);
						}
						else{
							pTemp.resetMLFHelper();
							processList[2].add(pTemp);
						}
					}
				}
				else if(!processList[2].isEmpty()){
					Process pTemp = processList[2].remove();
					localTime++;
					pTemp.decrementTimeLeft();
					if(pTemp.isFinished()){
						pTemp.setTime(localTime);
					}else{
						if(pTemp.MLFHelper()<3){
							pTemp.incrementMLFHelper();
							processList[2].addFirst(pTemp);
						}
						else{
							pTemp.resetMLFHelper();
							processList[1].add(pTemp);
						}
					}
				}
				else if(!processList[1].isEmpty()){
					Process pTemp = processList[1].remove();
					localTime++;
					pTemp.decrementTimeLeft();
					if(pTemp.isFinished()){
						pTemp.setTime(localTime);
					}else{
						if(pTemp.MLFHelper()<7){
							pTemp.incrementMLFHelper();
							processList[2].addFirst(pTemp);
						}
						else{
							pTemp.resetMLFHelper();
							processList[1].add(pTemp);
						}
					}
				}
				else if(!processList[0].isEmpty()){
					Process pTemp = processList[0].remove();
					localTime++;
					pTemp.decrementTimeLeft();
					if(pTemp.isFinished()){
						pTemp.setTime(localTime);
					}else{
						if(pTemp.MLFHelper()<15){
							pTemp.incrementMLFHelper();
							processList[0].addFirst(pTemp);
						}
						else{
							pTemp.resetMLFHelper();
							processList[0].add(pTemp);
						}
					}
				}
				if(localIndex < processes.size()) break;
			}
		}
		String result = writeOutput();
		reset();
		return result;
	}
}