package Dominion.appClasses;

/**
 * @author Joel Henz:
 * 
 */
public class GameHistory extends GameObject{
	
	private static final long serialVersionUID = 1;
	// Data included in a message
	private long id;
	// Generator for a unique message ID
	private static long messageID = 0;
	
	private String text;
	private GameParty party;
	private ActivateGUI activateGUI;
	private String sender;
	private boolean switchPlayer;
	
	private static long nextMessageID() {		
		return messageID++;
	}
	
	public GameHistory(String text, GameParty party,String sender){
		super(GameObject.ObjectType.GameHistory);
		this.text=text;
		this.party=party;
		this.sender=sender;
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
	
	public String getText(){
		return this.text;
	}
	
	public String getSender(){
		return this.sender;
	}
	
	public ActivateGUI getActivateGUI(){
		return this.activateGUI;
	}
	
	public void setActivateGUI(ActivateGUI activateGUI){
		this.activateGUI=activateGUI;
	}
	
	public boolean getSwitchPlayer(){
		return this.switchPlayer;
	}
	
	public void setSwitchPlayer(boolean switchPlayer){
		this.switchPlayer=switchPlayer;
	}

}