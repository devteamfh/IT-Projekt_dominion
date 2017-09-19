package Dominion.appClasses;

import java.io.Serializable;

/**
 * @author Joel Henz
 */
public class ChatMessage implements Serializable {
	
	private static final long serialVersionUID = 1; // This is version 1 of the message class
	// Data included in a message
	private long id;
	private String client;
	private String message;
	// Generator for a unique message ID
	private static long messageID = 0;
	 
	private static long nextMessageID() {		
		return messageID++;
	}
	 
	public ChatMessage(String client, String message) {
		this. id = -1;
		this.client = client;
		this.message = message;
	}
	  
	public String getName(){
		return this.client;
	}
	   
	public String getMsg (){
		return this.message;
	}
	   
	public void setID(){
		if (this. id == -1){
			this. id = nextMessageID();
		}
	}
	   
	public long getID(){
		return this.id;
	}
	  
}
