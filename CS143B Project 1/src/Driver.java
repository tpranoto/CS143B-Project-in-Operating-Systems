//Timothy Pranoto
//3896411
//CS143B Project 1
//Processes and Resources Management

import java.io.*;
import java.util.*;

public class Driver {
	
	private static final String path = "F:\\";
	private static final String FILEINPUT = "input.txt";
	private static final String FILEOUTPUT = "38964311.txt";

	public static void main(String[] args) throws IOException {
		Manager man = new Manager();
		BufferedReader fin = new BufferedReader(new FileReader(path+FILEINPUT));
		PrintStream fout = new PrintStream(new File(path+FILEOUTPUT));
		String line;
		fout.print(man.getCurrentProcess());

		while ((line = fin.readLine()) != null) {
			if (line.length() > 0) {
				try {
					StringTokenizer tok = new StringTokenizer(line);
					String command = tok.nextToken();
					if (command.equalsIgnoreCase("init")) {
						fout.println();
						man = new Manager();
						fout.print(man.getCurrentProcess());
					} else if (command.equalsIgnoreCase("cr")) {
						String param1 = tok.nextToken();
						int param2 = Integer.parseInt(tok.nextToken());
						if (man.createProcess(param1, param2))
							fout.print(man.getCurrentProcess());
						else
							fout.print("error ");
					} else if (command.equalsIgnoreCase("de")) {
						String param1 = tok.nextToken();
						if (man.destroyProcess(param1))
							fout.print(man.getCurrentProcess());
						else
							fout.print("error ");
					} else if (command.equalsIgnoreCase("req")) {
						String param1 = tok.nextToken();
						int param2 = Integer.parseInt(tok.nextToken());
						if (man.requestResource(param1, param2))
							fout.print(man.getCurrentProcess());
						else
							fout.print("error ");
					} else if (command.equalsIgnoreCase("rel")) {
						String param1 = tok.nextToken();
						int param2 = Integer.parseInt(tok.nextToken());
						if (man.releaseResource(param1, param2))
							fout.print(man.getCurrentProcess());
						else
							fout.print("error ");
					} else if (command.equalsIgnoreCase("to")) {
						man.time_out();
						fout.print(man.getCurrentProcess());
					}
				} catch (Exception e) {
					fout.print("error ");
				}
			}
		}
		fout.flush();
		fout.close();
		System.out.println("DONE");
	}
}
