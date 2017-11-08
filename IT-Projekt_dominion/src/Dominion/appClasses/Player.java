package Dominion.appClasses;

import java.io.ObjectOutputStream;

/**
 * @author Joel Henz
 */
public class Player {

	
	private int points = 3;
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
	
	

}
