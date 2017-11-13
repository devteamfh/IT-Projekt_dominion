package Dominion.appClasses;

import java.io.Serializable;

/**
 * @author Joel Henz
 */
public class GameObject implements Serializable {
	
	private static final long serialVersionUID = 1;
	// Data included in a message
	private long id;
	private ObjectType type;
	private static long messageID = 0;
	
	public enum ObjectType {
		 ChatMessageLobby, InformationObject,
		 GameParty, UpdateLobby, StartInformation,
		 JoinGameParty, UpdateGameParty
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
