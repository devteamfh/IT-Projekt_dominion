package Dominion.appClasses;

import java.io.Serializable;


/**
 * @author Joel Henz, David Steuri
 * this player class doesn't save the ObjectOutputStreams(OS) -->used on client side
 */
public class PlayerWithoutOS implements Serializable {

	private static final long serialVersionUID = 1;
	// Data included in a message
	private long id;
	// Generator for a unique message ID
	private static long messageID = 0;
	
	private int points = 3;
	private int numberOfActions =3; //David
	private int numberOfBuys=3; // David
	private int numberOfTreasures =0; // David
	private String username;
	
	private static long nextMessageID() {		
		return messageID++;
	}
	
	public PlayerWithoutOS(String username){
		this.id=-1;
		this.username = username;
	}
	
	//is needed for clone a Player list
	public PlayerWithoutOS(PlayerWithoutOS player){
		this.id=-1;
		this.username = player.getUsername();
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
	
	public int getNumberOfActions(){
		return this.numberOfActions;
	}
	
	public void increaseNumberOfActions(int i){
		this.numberOfActions = this.numberOfActions+i;
	}
	
	public void decreaseNumberOfActions(){
		this.numberOfActions--;
	}
	
	public int getNumberOfBuys(){
		return this.numberOfBuys;
	}
	
	public void increaseNumberOfBuys(int i){
		this.numberOfBuys = this.numberOfBuys+i;
	}
	
	public void decreaseNumberOfBuys(){
		this.numberOfBuys--;
	}
	
	public int getNumberOfTreasures(){
		return this.numberOfTreasures;
	}
	
	public void increaseNumberOfTreasures(int i){
		this.numberOfTreasures = this.numberOfTreasures+i;
	}
	
	public void decreaseNumberOfTreasures(int i){
		this.numberOfTreasures = numberOfTreasures-i;
	}
	
	public void setInitialActionsAndBuys(){
		this.numberOfActions = 3;
		this.numberOfBuys=3;
	}
	
	public void actionEnded(){
		this.numberOfActions =0;
	}

	public void buyEnded(){
		this.numberOfBuys=0;
	}
	
	public int getPoints(){
		return this.points;
	}
	
	public void increasePoints(){
		this.points++;
	}
	

}