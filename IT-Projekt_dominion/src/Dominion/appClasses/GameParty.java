package Dominion.appClasses;

import java.util.ArrayList;

public class GameParty extends GameObject{
	
	private static final long serialVersionUID = 1; // This is version 1 of the message class
	// Data included in a message
	private long id;
	// Generator for a unique message ID
	private static long messageID = 0;
	
	private String selectedMode;
	private String creator;
	private int numberOfPlayers;
	private int loggedInPlayers = 1;
	private ArrayList <Player> playerListOfGame;
	
	private static long nextMessageID() {		
		return messageID++;
	}
	
	public GameParty(String selectedMode, String creator, int number){
		super(GameObject.ObjectType.GameParty);
		this.selectedMode=selectedMode;
		this.creator=creator;
		this.numberOfPlayers=number;
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
		return this.creator+", "+this.selectedMode+", "+this.numberOfPlayers;
	}
	
	public void addPlayer(Player player){
		this.playerListOfGame.add(player);
	}

}
