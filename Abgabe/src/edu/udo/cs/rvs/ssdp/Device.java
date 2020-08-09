
package edu.udo.cs.rvs.ssdp;

import java.util.LinkedList;

/**
 * 
 * Device class is used to store one Device, its UUID and Services
 * 
 * @author marcel.bienia
 *
 */

public class Device {
	
	//used to store the device UUID
	private String uuid = "default";
	//used to store the device Services
	private LinkedList<String> dienste = new LinkedList<>();
	// used to store if the device is logging out
	private boolean abmelden;
	
	
	/**
	 * Constructor
	 * @param uuid is the UUID for the device that is going to be generatet
	 */
	public Device (String uuid) {
		this.uuid = uuid;
		abmelden = false;
	}
	
	/**
	 * Constructor for devices with an UUID that will be added later
	 */
	public Device() {

	}
	
	/**
	 * adding a new Service to the list of services of this device
	 * @param dienst is the new service
	 */
	public void addDienst(String dienst) {
		dienste.add(dienst);
	}
	
	/**
	 * @return the list of all currently known services
	 */
	public LinkedList<String> getDienste() {
		return dienste;
	}
	
	/**
	 * @return the device UUID
	 */
	public String getUuid() {
		return uuid;
	}
	
	/**
	 * changing the way a device is printed to the console
	 */
	@Override
	public String toString() {
		String result = uuid + "   -   ";
		
		if(dienste.size()==1) {
			result = result + dienste.getFirst();
		}	else {
			for(String s:dienste) {
				result = result + s + ", ";
			}
		}
		return result;
	}
	
	/**
	 * assigns an uuid if one has not already been assigned
	 * @param uuid
	 */
	public void addUuid(String uuid) {
		// checks if one uuid already has been assigned
		if(this.uuid.equals("default")) {
			this.uuid = uuid;
		}
	}
	
	/**
	 * noting that an device is logging of
	 */
	public void abmelden() {
		abmelden = true;
	}
	
	/**
	 * @return if the device is logging of
	 */
	public boolean getAbmelden() {
		return abmelden;
	}
	
	/**
	 * changing the way two devices are compared
	 * two devices are equal / the same if they have the same uuid 
	 */
	@Override
	public boolean equals(Object o) {
        // If the object is compared with itself then return true   
        if (o == this) { 
            return true; 
        } 
  
        /* Check if o is an instance of Device or not 
          "null instanceof [type]" also returns false */
        if (!(o instanceof Device)) { 
            return false; 
        } else {
        	return this.getUuid().equals(((Device) o).getUuid());
        }
        
	}

}
