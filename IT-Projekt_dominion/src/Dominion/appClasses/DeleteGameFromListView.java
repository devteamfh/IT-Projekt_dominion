package Dominion.appClasses;

/**
 * @author Joel Henz
 */
public class DeleteGameFromListView extends GameObject {

	private static final long serialVersionUID = 1;
	// Data included in a message
	private long id;
	// Generator for a unique message ID
	private static long messageID = 0;
	
	private GameParty gamePartyToDelete;
	
	private static long nextMessageID() {		
		return messageID++;
	}
	
	public DeleteGameFromListView(GameParty gameParty){
		super(GameObject.ObjectType.DeleteGameFromListView);
		this.gamePartyToDelete=gameParty;
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
	
	public GameParty getGamePartyToDelete(){
		return this.gamePartyToDelete;
	}
}
