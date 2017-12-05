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
	private String sender;
	private PlayerWithoutOS playerForGUIActivation;
	private PlayerWithoutOS currentPlayer;
	private HistoryType type;
	
	public enum HistoryType {
		 EndAction, EndBuy
		 };
	
	private static long nextMessageID() {		
		return messageID++;
	}
	
	public GameHistory(String text, GameParty party,PlayerWithoutOS currentPlayer, HistoryType type){
		super(GameObject.ObjectType.GameHistory);
		this.id=-1;
		this.text=text;
		this.party=party;
		this.currentPlayer=currentPlayer;
		this.type=type;
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
	
	public void updateText(String text){
		this.text = this.text+"\n"+text;
	}
	
	public String getSender(){
		return this.sender;
	}
	
	public HistoryType getHistoryType(){
		return this.type;
	}
	
	public void setPlayerForGUIActivation(PlayerWithoutOS user){
		this.playerForGUIActivation=user;
	}
	
	public PlayerWithoutOS getPlayerForGUIActivation(){
		return this.playerForGUIActivation;
	}
	
	public void setCurrentPlayer(PlayerWithoutOS user){
		this.currentPlayer=user;
	}
	
	public PlayerWithoutOS getCurrentPlayer(){
		return this.currentPlayer;
	}

}