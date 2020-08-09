package edu.udo.cs.rvs.ssdp;

import java.util.LinkedList;

/**
 * the worker thread edits all devices from the arriving unicast and multicast packages to an list of all current active devices
 * @author marcel.bienia
 */


public class Worker implements Runnable {
	
	// the listener 
	private Listen listener;
	// the arriving packages 
	private LinkedList<String> nachrichten;
	// list of all 
	private LinkedList<Device> geraete = new LinkedList<>();
		
	/**
	 * constructor
	 * 
	 * @param li is the listener Object that makes the List of arriving packages
	 */
	public Worker(Listen li) {
		this.listener = li;
	}
	
	
	public void run() {
		
		while(true) {
			
			// getting the current list of packages
			nachrichten = listener.getList();
			// making a second list that can be changed in this thread without synchronization 
			LinkedList<String> workingList = new LinkedList<>();
			
			//removing all packages from the synchronized list to the second list
			synchronized(nachrichten) {
				while(!nachrichten.isEmpty()) {
					workingList.add(nachrichten.removeFirst());
				}
			}
			
			
			String[] result;
			String[] line;
			String[] services;
			Device devi = new Device();
			
			// editing the List as long as its still contains packages
			while(!workingList.isEmpty()) {
				
				// splitting the current messages at the end of every line
				result = workingList.removeFirst().split("\r\n");
				
				// if the package is an unicast
				if(result[0].compareTo("HTTP/1.1 200 OK")==0) {

					// then creating an device Object with uuid and services
					for (int i = 1; i<result.length; i++) {
						line = result[i].split(":");
						services = result[i].split(":",2); 
						
						// uuid
						if(line[0].equals("USN")) {
							devi.addUuid(line[2].trim());
						}	
						// services
						else if(line[0].equals("ST")) {
							devi.addDienst(services[1].trim());
						}
					}
					// adding the new found device to the list of devices
					hinzufuegen(devi);
				} 
				// if the package is an multicast
				else if(result[0].compareTo("NOTIFY * HTTP/1.1")==0) {
				
					// then creating an device Object with uuid, services and logging in or out
					for (int i = 1; i<result.length; i++) {
						line = result[i].split(":");
						services = result[i].split(":",2); 

						//uuid
						if(line[0].equals("USN")) {
							devi.addUuid(line[2].trim());
						}	
						
						// logging in or out
						else if(line[0].equals("NTS") && line[2].equals("byebye")) {	
							devi.abmelden();
						}	
						
						// services
						else if(line[0].equals("NT")) {
							devi.addDienst(services[1].trim());
						}
					}
					// adding the new found device to the list of devices
					hinzufuegen(devi);
				}

			}
			
			//waiting
			try {
				Thread.sleep(10);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * adding the new found device to the list of devices
	 * @param d is the device that should be added to the list of devices
	 */
	public void hinzufuegen(Device d) {

		synchronized(geraete) {
			// if device is sending a "logging of" then removing or not adding it
			if(d.getAbmelden()) {
				if(geraete.contains(d)) {
					geraete.remove(d);
				}
				//else do nothing
			}	
			// device should be added
			else {
				// adding the device if it is not already in the List
				if(!geraete.contains(d)) {
					geraete.add(d);
				}	
				// if it is already in the List then just adding its new service (if it is new)
				else {
					int index = geraete.indexOf(d);
					Device devi = geraete.get(index);
					if(!devi.getDienste().contains(d.getDienste().getFirst())) {
						devi.addDienst(d.getDienste().getFirst());
					}
				}
			}

		}
	}
	
	/**
	 * @return the list of current active devices 
	 */
	public LinkedList<Device> getGeraete() {
		synchronized(geraete) {
			return geraete;
		}
	}
	
	/**
	 * deleting all current devices
	 */
	public void clearGeraete() {
		synchronized(geraete) {
			geraete = new LinkedList<Device>();
		}
	}
}
