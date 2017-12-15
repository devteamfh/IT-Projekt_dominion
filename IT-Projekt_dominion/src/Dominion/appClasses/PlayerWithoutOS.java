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
	
	public int getPoints(){
		return this.points;
	}
	
	public void increasePoints(int points){
		this.points= this.points+points;
	}
	
	public void setPoints(int points){
		this.points=points;
	}
	

}