//Timothy Pranoto
//38964311
//CS143B Project 2

public class Process {
	private int pid;
	private int start;
	private int end;
	private int timeLeft;
	private int duration; 
	private int MLFH;

	public Process(int pid, int start, int time){
		this.pid = pid;
		this.start = start;
		timeLeft = time;
		duration = time;
		MLFH=0;
	}
	public int pid(){
		return pid;
	}
	public int startTime(){
		return start;
	}
	public int MLFHelper(){
		return MLFH;
	}
	public void setTime(int finish){
		end = finish;
	}
	public int duration(){
		return duration;
	}
	public int timeLeft(){
		return timeLeft;
	}
	
	public void resetMLFHelper(){
		MLFH=0;
	}
	public void incrementMLFHelper(){
		MLFH++;
	}
	public void decrementTimeLeft(){
		timeLeft--;
	}
	public boolean isFinished(){
		return timeLeft == 0;
	}
	public int turnAroundTime(){
		return end - start;
	}
	public void reset(){
		timeLeft = duration;
		end = start;
	}
}