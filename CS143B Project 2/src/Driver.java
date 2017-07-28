//Timothy Pranoto
//38964311
//CS143B Project 2

import java.io.*;
import java.util.*;

public class Driver {
	private static final String path = "F:\\";
	private static final String FILEINPUT = "input.txt";
	private static final String FILEOUTPUT = "38964311.txt";

	public static void main(String[] args) throws IOException {
		ArrayList<Process> pl = new ArrayList<Process>();
		BufferedReader fin = new BufferedReader(new FileReader(path+FILEINPUT));
		PrintStream fout = new PrintStream(new File(path+FILEOUTPUT));
		String line;
		int index=0;
		
		if((line = fin.readLine()) != null) {
			if (line.length() > 0) {
				StringTokenizer tok = new StringTokenizer(line);
				while(tok.hasMoreTokens()){
					int param1=Integer.parseInt(tok.nextToken());
					int param2=Integer.parseInt(tok.nextToken());
					pl.add(new Process(index,param1,param2));
				}
			}
		}
		Scheduler s = new Scheduler(pl);
		fout.println(s.FIFO());
		fout.println(s.SJF());
		fout.println(s.SRT());
		fout.println(s.MLF());
		System.out.println("DONE");
	}
}