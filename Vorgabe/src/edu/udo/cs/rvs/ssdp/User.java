package edu.udo.cs.rvs.ssdp;

import java.util.Scanner;

/**
 * the User Thread listens for the incoming commands from the user and reacts to them 
 * @author marce.bienia
 *
 */

public class User implements Runnable {
	
	// worker Thread used to get the List of Devices
	private Worker worker;
	// Listen Thread used to send scan messages
	private Listen listener;
	
	/**
	 * constructor
	 * @param li is the listening Thread
	 * @param wo is the translating worker Thread
	 */
	public User(Listen li, Worker wo) {
		this.listener = li;
		this.worker = wo;
	}
	
	public void run() {
		
		while(true) {
			
			@SuppressWarnings("resource")
			// scanning for incoming commands and saving them in current line
			Scanner s = new Scanner(System.in);
			String currentLine = s.next();
			
			// ending the program
			if(currentLine.equals("EXIT")) {
				
				
				listener.end();
				System.out.println("ending");
				System.exit(0);
				
				
			}	
			
			// deleting all current devices from the List
			else if(currentLine.equals("CLEAR")) {
				
				
				worker.clearGeraete();
				
				
			}	
			
			// printing out the List of current devices from the worker thread
			else if(currentLine.equals("LIST")) {
				
				
				for(Device d:worker.getGeraete()) {
					System.out.println(d);
				}
				
				
			}	
			
			// sending the sacn message from the worker thread
			else if(currentLine.equals("SCAN")) {
				
				listener.send();
				
			}
			
//			waiting
			try {
				Thread.sleep(10);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
	}
}
