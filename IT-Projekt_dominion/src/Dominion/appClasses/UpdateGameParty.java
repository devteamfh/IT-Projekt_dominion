package Dominion.appClasses;

public class UpdateGameParty extends GameObject {
	
	private GameParty party;
	
	private static final long serialVersionUID = 1;
	// Data included in a message
	private long id;
	// Generator for a unique message ID
	private static long messageID = 0;
	
	private static long nextMessageID() {		
		return messageID++;
	}
	
	public UpdateGameParty(GameParty party){
		super(GameObject.ObjectType.UpdateGameParty);
		this.party=party;
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
	
	public GameParty getGameParty(){
		return this.party;
	}

}
