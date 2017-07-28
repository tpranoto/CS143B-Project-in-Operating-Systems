//Timothy Pranoto
//38964311
//CS143B Project 3

import java.io.*;
import java.util.*;

public class Driver {
	private static final String path = "F:\\";
	private static final String FILEINPUT1 = "input1-simple.txt";
	private static final String FILEINPUT2 = "input2-simple.txt";
	private static final String FILEOUTPUT1 = "38964311-notlb.txt";
	private static final String FILEOUTPUT2 = "38964311-tlb.txt";
	
	public static void main(String[] args) throws IOException{
		Scanner fin1 = new Scanner(new File(path+FILEINPUT1));
		Scanner fin2 = new Scanner(new File(path+FILEINPUT2));
		
		FileWriter fout1 = new FileWriter(path+FILEOUTPUT1);
		FileWriter fout2 = new FileWriter(path+FILEOUTPUT2);
		
		BufferedWriter w1 = new BufferedWriter(fout1);
		BufferedWriter w2 = new BufferedWriter(fout2);
		
		StringBuilder sb1 = new StringBuilder();
		StringBuilder sb2 = new StringBuilder();
		
		String line1 = fin1.nextLine();
		PM PM1 = new PM(false); 
		PM PM2 = new PM(true);
		String line2 = fin1.nextLine();
		PM1.init(line1,line2);
		PM2.init(line1,line2);
		
		String line3 = fin2.nextLine();
		sb1.append(PM1.translate(line3));
		sb2.append(PM2.translate(line3));	
		
		fin1.close();
		fin2.close();
		
		w1.write(sb1.toString());
		w2.write(sb2.toString());
		
		w1.close();
		w2.close();
		fout1.close();
		fout2.close();
		System.out.println("DONE");
	}
}