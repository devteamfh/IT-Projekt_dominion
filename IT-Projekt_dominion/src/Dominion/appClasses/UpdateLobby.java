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
	private ArrayList <GameParty> listOfOpenGames = new ArrayList <GameParty>();
	
	private ArrayList <StartInformation> al_startInformation = new ArrayList<StartInformation>();
	
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
	
	public void setListOfOpenGames(ArrayList <GameParty> list){
		this.listOfOpenGames = list;
	}
	
	public ArrayList <GameParty> getListOfOpenGames(){
		return this.listOfOpenGames;
	}
	
	//PlayerStatistics
	public ArrayList <StartInformation> getListOfStartInformation(){
		return this.al_startInformation;
	}
	//PlayerStatistics
	public void setListOfStartInformation(ArrayList <StartInformation> al_startInformation){
		this.al_startInformation = al_startInformation;
	}


}