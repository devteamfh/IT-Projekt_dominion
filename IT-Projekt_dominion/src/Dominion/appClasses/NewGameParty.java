package Dominion.appClasses;

import java.util.ArrayList;

/**
 * @author Joel Henz
 */
public class NewGameParty extends GameObject{
	
	private static final long serialVersionUID = 1;
	// Data included in a message
	private long id;
	// Generator for a unique message ID
	private static long messageID = 0;
	
	private String selectedMode;
	private String creator;
	private int numberOfPlayers;
	private int loggedInPlayers = 0;
	private ArrayList <Player> playerListOfGame;
	
	private static long nextMessageID() {		
		return messageID++;
	}
	
	public NewGameParty(String selectedMode, String creator, int number){
		super(GameObject.ObjectType.NewGameParty);
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
		return this.creator+", "+this.selectedMode+", "+this.loggedInPlayers+" Spieler von, "+this.numberOfPlayers;
	}
	
	public void addPlayer(Player player){
		this.playerListOfGame.add(player);
		this.loggedInPlayers++;
	}
	
	public String getCreator(){
		return this.creator;
	}

}