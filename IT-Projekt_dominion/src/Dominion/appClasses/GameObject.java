package Dominion.appClasses;

import java.io.Serializable;

public class GameObject implements Serializable {
	
	private static final long serialVersionUID = 1; // This is version 1 of the message class
	// Data included in a message
	private long id;
	private ObjectType type;
	private static long messageID = 0;
	
	public enum ObjectType {
		 ChatMessage, InformationObject,
		 };
		 
	public GameObject(ObjectType type){
		this.type = type;
		this. id = -1;
	}

	private static long nextMessageID() {		
		return messageID++;
	}
	
	public ObjectType getType(){
		return this.type;
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
