package Dominion.appClasses;

import java.util.ArrayList;

/**
 * @author Joel Henz
 */
public class UpdateLobby extends GameObject {
	

	private static final long serialVersionUID = 1;
	// Data included in a message
	private long id;
	// Generator for a unique message ID
	private static long messageID = 0;
	private ArrayList <NewGameParty> listOfOpenGames = new ArrayList <NewGameParty>();
	
	private static long nextMessageID() {		
		return messageID++;
	}
	
	
	public UpdateLobby() {
		super(GameObject.ObjectType.UpdateLobby);
		
	}
	
	public void setID(){
		if (this. id == -1){
			this. id = nextMessageID();
		}
	}
	
	public long getID(){
		return this.id;
	}
	
	public void setListOfOpenGames(ArrayList <NewGameParty> list){
		this.listOfOpenGames = list;
	}
	
	public ArrayList <NewGameParty> getListOfOpenGames(){
		return this.listOfOpenGames;
	}

}