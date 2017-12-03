package Dominion.appClasses;

import java.io.ObjectOutputStream;

/**
 * @author Joel Henz, David Steuri
 * this player class saves the ObjectOutputStreams(OS)
 */
public class PlayerWithOS {

	
	private int points = 3;
	private int numberOfActions =1; //David
	private int numberOfBuys=1; // David
	private int numberOfTreasures =0; // David
	private String username;
	private ObjectOutputStream out;
	
	public PlayerWithOS(String username, ObjectOutputStream out){
		this.username = username;
		this.out = out;
	}
	
	public ObjectOutputStream getOut(){
		return this.out;
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
	

}
