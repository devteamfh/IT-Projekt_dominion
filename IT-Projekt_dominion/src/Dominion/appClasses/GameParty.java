package Dominion.appClasses;

import java.util.ArrayList;

/**
 * @author Joel Henz
 * instances of this class are sent/read by client and server. The equivalent class of GameParty is GamePartyOnServer. Objects of this class are only used on server side (to store Player objects and ObjectOutputStream-objects) and will not be sent
 * while communication
 */
public class GameParty extends GameObject{
	
	private static final long serialVersionUID = 1;
	// Data included in a message
	private long id;
	// Generator for a unique message ID
	private static long messageID = 0;
	
	private String selectedMode;
	private PlayerWithoutOS host;
	private int numberOfMaxPlayers;
	private int numberOfLoggedInPlayers = 0;
	private ArrayList <PlayerWithoutOS> playersOfThisGameParty = new ArrayList <PlayerWithoutOS>();
	private int rounds;
	private boolean gameHasStarted;
	
	private static long nextMessageID() {		
		return messageID++;
	}
	
	public GameParty(String selectedMode, PlayerWithoutOS host, int number){
		super(GameObject.ObjectType.GameParty);
		this.selectedMode=selectedMode;
		this.host=host;
		this.numberOfMaxPlayers=number;
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
		if(this.withRounds()){
			return "Host: "+this.host.getUsername()+", "+this.selectedMode+": "+this.rounds+", "+this.numberOfLoggedInPlayers+" Spieler von "+this.numberOfMaxPlayers+" eingeloggt";
		}else{
			return "Host: "+this.host.getUsername()+", "+this.selectedMode+", "+this.numberOfLoggedInPlayers+" Spieler von "+this.numberOfMaxPlayers+" eingeloggt";
		}
	}
	
	public PlayerWithoutOS getHost(){
		return this.host;
	}
	
	//adding the player who is entering an existing GameParty (choosing from the ListView in the lobby)
	public void addNewPlayer(PlayerWithoutOS player){
		this.playersOfThisGameParty.add(player);
		this.numberOfLoggedInPlayers++;
	}
	
	public void removePlayer(PlayerWithoutOS player){
		
		//searching the player to remove and remove him
		for(int i=0; i<this.playersOfThisGameParty.size();i++){
			if(this.playersOfThisGameParty.get(i).getUsername().equals(player.getUsername())){
				this.playersOfThisGameParty.remove(i);
				}
		}
		
		this.numberOfLoggedInPlayers--;
		
	}
	
	public int getMaxNumberOfPlayers(){
		return this.numberOfMaxPlayers;
	}
	
	public int getNumberOfLoggedInPlayers(){
		return this.numberOfLoggedInPlayers;
	}
	
	public ArrayList <PlayerWithoutOS> getArrayListOfPlayers(){
		return this.playersOfThisGameParty;
	}
	
	public boolean isFull(){
		return this.numberOfLoggedInPlayers == this.numberOfMaxPlayers;
	}
	
	public boolean withRounds(){
		return this.selectedMode.equals("Rundenanzahl");
	}
	
	public void setRounds (int rounds){
		this.rounds=rounds;
	}
	
	public void setGameHasStarted(boolean gameHasStarted){
		this.gameHasStarted=gameHasStarted;
	}
	
	public boolean getGameHasStarted(){
		return this.gameHasStarted;
	}
	

}