package Dominion.appClasses;

/**
 * @author Joel Henz
 */
public class CancelGame extends GameObject {
	private static final long serialVersionUID = 1;
	// Data included in a message
	private long id;
	// Generator for a unique message ID
	private static long messageID = 0;
	
	private GameParty gamePartyToCancel;

	
	private static long nextMessageID() {		
		return messageID++;
	}
	
	public CancelGame(GameParty gamePartyToCancel){
		super(GameObject.ObjectType.CancelGame);
		this.gamePartyToCancel=gamePartyToCancel;
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
		return this.gamePartyToCancel;
	}
	
}
