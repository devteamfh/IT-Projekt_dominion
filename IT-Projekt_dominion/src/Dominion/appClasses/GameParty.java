package Dominion.appClasses;

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
	private String player;
	private int numberOfPlayers;
	private int loggedInPlayers = 0;
	
	private static long nextMessageID() {		
		return messageID++;
	}
	
	public GameParty(String selectedMode, String creator, int number,String player){
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
		return this.creator+", "+this.selectedMode+", "+this.loggedInPlayers+" Spieler von, "+this.numberOfPlayers;
	}
	
	public String getCreator(){
		return this.creator;
	}
	
	public String getPlayer(){
		return this.player;
	}

}