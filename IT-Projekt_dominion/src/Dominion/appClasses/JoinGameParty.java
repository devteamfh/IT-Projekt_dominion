package Dominion.appClasses;

/**
 * @author Joel Henz
 */
public class JoinGameParty extends GameObject {
	
	private static final long serialVersionUID = 1;
	// Data included in a message
	private long id;
	// Generator for a unique message ID
	private static long messageID = 0;
	
	GameParty selectedGameParty;
	String username;
	
	public JoinGameParty(GameParty gamePartyToJoin, String user){
		super(GameObject.ObjectType.JoinGameParty);
		this.username=user;
		this.selectedGameParty=gamePartyToJoin;	
		this. id = -1;
	}
	
	private static long nextMessageID() {		
		return messageID++;
	}
	
	public void setID(){
		if (this. id == -1){
			this. id = nextMessageID();
		}
	}
	
	public long getID(){
		return this.id;
	}
	
	public GameParty getSelectedGameParty(){
		return this.selectedGameParty;
	}
	
	public String getUsername(){
		return this.username;
	}

}
