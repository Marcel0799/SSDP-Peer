package edu.udo.cs.rvs.ssdp;


/**
 * This class is first instantiated on program launch and IF (and only if) it
 * implements Runnable, a {@link Thread} is created and started.
 * 
 * @author marcel.bienia
 */

public class SSDPPeer implements Runnable
{
	
	/**
	 * standard constructor 
	 */	
	public SSDPPeer()
	{

	}
	
	

	/**
	 *run method is necessary for an implementation of runnable. It starts Listener, worker and user thread. 
	 */
	@Override
	public void run() {
		
		Listen listener = new Listen();;
		Worker worker;
		User user;
		
		// Check if peer can be started as a Thread
		if (listener instanceof Runnable)
		{
			/* make ListenThread
			* Construct the Thread and start it (also set some properties)
			*/
			Runnable listenerRunnable = (Runnable) listener;
			Thread listenerThread = new Thread(listenerRunnable);
			listenerThread.setName("Listen Thread");
			listenerThread.setDaemon(false);
			listenerThread.start();
			
			/** make WorkerThread
			* Construct the Thread with giving him access to the listenerThread and start it (also set some properties)
			*/
			worker = new Worker(listener);
			Runnable wothRunnable = (Runnable) worker;
			Thread wothThread = new Thread(wothRunnable);
			wothThread.setName("Worker Thread");
			wothThread.setDaemon(false);
			wothThread.start();
			
			/** make UserThread
			* Construct the Thread with giving him access to the listenerThread and WorkerThred 
			* and start it (also set some properties)
			*/
			user = new User(listener,worker);
			Runnable userRunnable = (Runnable) user;
			Thread userThread = new Thread(userRunnable);
			userThread.setName("User Thread");
			userThread.setDaemon(false);
			userThread.start();
	
		}
		
	}
}
