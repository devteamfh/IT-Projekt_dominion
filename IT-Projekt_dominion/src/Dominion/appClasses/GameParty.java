package Dominion.appClasses;

import java.util.ArrayList;

/**
 * @author Joel Henz
 * instances of this class are sent/read by client and server. The equivalent class of GameParty is GamePartyOnServer and is used only on server side (to store Player objects and ObjectOutputStream-objects)
 */
public class GameParty extends GameObject{
	
	private static final long serialVersionUID = 1;
	// Data included in a message
	private long id;
	// Generator for a unique message ID
	private static long messageID = 0;
	
	private String selectedMode;
	private String creator;
	private int numberOfMaxPlayers;
	private int numberOfLoggedInPlayers = 0;
	private String [] playersOfThisGameParty;
	
	private static long nextMessageID() {		
		return messageID++;
	}
	
	public GameParty(String selectedMode, String creator, int number){
		super(GameObject.ObjectType.GameParty);
		this.selectedMode=selectedMode;
		this.creator=creator;
		this.numberOfMaxPlayers=number;
		this.playersOfThisGameParty = new String [this.numberOfMaxPlayers];
		this. id = -1;	
	}
	
	public void setID(){
		if (this. id == -1){
			this. id = nextMessageID();
		}
	}
	
	public long getID(){
		return this.id;
	}
	
	public String toString(){
		return "Host: "+this.creator+", "+this.selectedMode+", "+this.numberOfLoggedInPlayers+" Spieler von "+this.numberOfMaxPlayers+" eingeloggt";
	}
	
	public String getCreator(){
		return this.creator;
	}
	
	//adding the player who is entering an existing GameParty (choosing from the ListView in the lobby)
	public void addNewPlayer(String player){
		this.playersOfThisGameParty[numberOfLoggedInPlayers] = player;
		numberOfLoggedInPlayers++;
	}
	
	public int getMaxNumberOfPlayers(){
		return this.numberOfMaxPlayers;
	}
	
	public int getLoggedInPlayers(){
		return this.numberOfLoggedInPlayers;
	}
	
	public String[] getArrayOfPlayers(){
		return this.playersOfThisGameParty;
	}
	

}