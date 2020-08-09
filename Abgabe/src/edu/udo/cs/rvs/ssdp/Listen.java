package edu.udo.cs.rvs.ssdp; 
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.UUID;

/**
 * Listen thread which listens for incoming datagram-packages
 * 
 * @author marcel.bienia
 *
 */
public class Listen implements Runnable{
	
	// boolean that terminates the while loop if necessary
	private boolean running;
	private MulticastSocket ms;
	// making the List that will contain all arriving messages
	private LinkedList<String> list = new LinkedList<>();
	
	/**
	 * standard constructor
	 * sets running to true so the while loop can start
	 * generating the Socket so we can read the arriving messages 
	 */
	public Listen() {
		running = true;
		
		try {		
			ms = new MulticastSocket(1900);
			ms.joinGroup(InetAddress.getByName("239.255.255.250"));			
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
		
	}
	
	/**
	 * listening for messages and adding to the list 
	 */
	public void run() {
		
		 // buffer that will contain the arriving bytes	
		 byte[] buf = new byte[1000];
		 // package used to safe the arriving packages 
		 DatagramPacket paket = new DatagramPacket(buf, buf.length);
		
		 while(running) {
			
			// receiving the current package
			 try {
				ms.receive(paket);
			} catch (Exception e) {
				e.printStackTrace();
				System.exit(1);
			}
			 
			// translating the package from byte to String in UTF8
			String message = new String(paket.getData(), StandardCharsets.UTF_8);
			
			//adding the current package
			synchronized(list) {
				list.add(message);
			}
			
			//waiting
			try {
				Thread.sleep(10);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		 
//  	end Socket 
		try {
			ms.leaveGroup(InetAddress.getByName("239.255.255.250"));
			ms.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
//	stopping the while loop
	public void end() {
		running = false;
	}
	
/**
 * @return the current list auf packages
 */
	public LinkedList<String> getList() {
		
		return list;
	}
	
/**
 * sending the "Suchanfrage" 
 */
	public void send() {
		
		// creating a random uuid
		UUID id = UUID.randomUUID();
		
		// the message with the uuid
		String scan = "M-SEARCH * HTTP/1.1\r\n" + 
				"S: uuid:" + id + "\r\n" +
				"HOST: 239.255.255.250:1900\r\n" + 
				"MAN: ssdp:discover \r\n" + 
				"ST: ge:fridge\r\n" +
				"\r\n";
		
		// creating the package and sending it
        try {
			DatagramPacket scanner = new DatagramPacket(scan.getBytes("utf8"), scan.getBytes("utf8").length,InetAddress.getByName("239.255.255.250"),1900);
			ms.send(scanner);
        } catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

}
