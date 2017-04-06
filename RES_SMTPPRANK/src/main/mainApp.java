package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Random;

import smtp.SMTPHandler;

public class mainApp {
	
	private final static File victims = new File("src/data/victims.txt");
	private final static File messages = new File("src/data/messages.txt");
	private static int nbrGroup = 3;

	public static void main(String[] args) {
		SMTPHandler handler = new SMTPHandler("localhost", 25);
		System.out.println("Welcome to the Prank RES Application");
		
		if(args.length > 0 && args[0] != null && Integer.valueOf(args[0]) > 0)
		{
			nbrGroup = Integer.valueOf(args[0]);
		}
		
		// reading victims file
		LinkedList<String> victimsList = getVictims(victims);
		
		// reading message file
		LinkedList<String[]> messagesList = getMessages(messages);
		
		// Split by group and send the emails
		int victimSize = victimsList.size();
		int step = victimSize / nbrGroup;
		Random r = new Random();
		for (int i = 0; i < nbrGroup; i++) 
		{
			// range from i*step to (i+1) * step
			String from = victimsList.get(i*step);
			String[] to = new String[step-1];
			
			for (int j = 1; j < step; j++) 
			{
				to[j-1] = victimsList.get(i*step + j);
			}
			
			// and we send the email
			int randomMessage = r.nextInt(messagesList.size());
			handler.sendMail(from,
					to,
					messagesList.get(randomMessage)[0],
					messagesList.get(randomMessage)[1]);
		}
	}
	
	private static LinkedList<String> getVictims(File victims){
		BufferedReader br = null;
		LinkedList<String> vl = new LinkedList<>();
		try {
			br = new BufferedReader(new FileReader(victims));
			String line = br.readLine();
			while(line != null)
			{
				vl.add(line);
				line = br.readLine();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch(IOException e) {
			e.printStackTrace();
		}
		return vl;
	}
	
	private static LinkedList<String[]> getMessages(File messages)
	{
		BufferedReader br = null;
		LinkedList<String[]> messagesList = new LinkedList<>();
		try {
			br = new BufferedReader(new FileReader(messages));
			String line;
			while((line = br.readLine()) != null)
			{
				String[] m = new String[2];
				m[0] = line;
				br.readLine(); // skip empty line
				String emailMessage = "";
				while((line = br.readLine()) != null && !line.equals("."))
				{
					emailMessage += line + "\n";
				}
				m[1] = emailMessage;
				messagesList.add(m);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch(IOException e) {
			e.printStackTrace();
		}
		
		return messagesList;
	}
}
