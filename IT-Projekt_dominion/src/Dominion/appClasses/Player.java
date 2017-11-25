package Dominion.appClasses;

import java.io.ObjectOutputStream;

/**
 * @author Joel Henz, David Steuri
 */
public class Player {

	
	private int points = 3;
	private int numberOfActions; //David
	private int numberOfBuys; // David
	private int numberOfTreasures; // David
	private String username;
	private transient ObjectOutputStream out;
	
	public Player(String username, ObjectOutputStream out){
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
	
	
	

}
