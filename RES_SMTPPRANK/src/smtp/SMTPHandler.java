package smtp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class SMTPHandler {
	private String host;
	private int port;
	
	private Socket clientSocket = null;
	private BufferedReader reader = null;
    private PrintWriter writer = null;
	
	public SMTPHandler(String host, int port) {
		this.host = host;
		this.port = port;
	}
	
	public void sendMail(String from, String[] tos, String subject, String message)
	{
	    // connect via TCP to the server
		try {
			clientSocket = new Socket(host, port);
			reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			writer = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("connection successful");
		
	   if(clientSocket != null && reader != null && writer != null){
		   try {
			   		// prepare the email
			   		printCommand("EHLO Tony");
			        printCommand("MAIL From: " + from);
			        for(String to : tos)
			        {
			           	printCommand("RCPT To: " + to);
			        }
			        printCommand("DATA");
			        writer.println("From: " + from);
			        System.out.println("From: " + from);
			        for(String to : tos)
			        {
			           	writer.println("To: " + to);
			           	System.out.println("To: " + to);
			        }
			        writer.println("Subject: " + subject + "\n");
			        System.out.println("Subject: " + subject);
			        writer.println(message); // message body
			        System.out.println("message: " + message);
			        // send it
			        writer.println(".\n");
			        writer.flush();
			        
			        System.out.println(reader.readLine());
			        printCommand("QUIT");
		   }
			catch (Exception e) {
				e.printStackTrace();
			}
		finally {
			try {
				reader.close();
				writer.close();
				clientSocket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	   }
	}

	private void printCommand(String value)
	{
		
   		try {
   			writer.println(value); 
   			writer.flush();
   			System.out.println("Command : " + value);
			System.out.println(reader.readLine());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
