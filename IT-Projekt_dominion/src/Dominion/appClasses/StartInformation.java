package Dominion.appClasses;

import javafx.beans.property.SimpleStringProperty;

/**
 * @author Joel Henz
 */
public class StartInformation extends GameObject {
	
	private static final long serialVersionUID = 1;
	// Data included in a message
	private long id;
	// Generator for a unique message ID
	private static long messageID = 0;
	
	private String username;
	private String PW;
	
	private  SimpleStringProperty att2;
	
	private static long nextMessageID() {		
		return messageID++;
	}
	
	public StartInformation(String username) {
		super(GameObject.ObjectType.StartInformation);
		this.username = username;
		
	}
	
	public void setID(){
		if (this. id == -1){
			this. id = nextMessageID();
		}
	}

	
	public long getID(){
		return this.id;
	}
	
	public String getUsername(){
		return this.username;
	}
	
	public String getPW(){
		return this.PW;
	}

	public int getLoggedInPlayers() {
		// TODO Auto-generated method stub
		return 0;
	}
	


}
