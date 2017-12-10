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
	private PlayerWithoutOS playerForGUIActivation;
	private PlayerWithoutOS currentPlayer;
	private PlayerWithoutOS leavingPlayer;
	private PlayerWithoutOS winner;
	private HistoryType type;
	
	public enum HistoryType {
		 PlayAction, PlayBuy, EndAction,
		 EndBuy, LeaveGame, UpdateLobbyAfterLeave,
		 EndGame
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
		this.text = text;
	}
	
	public void clearText(){
		this.text = "";
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
	
	public PlayerWithoutOS getLeavingPlayer(){
		return this.leavingPlayer;
	}
	
	public void setLeavingPlayer(PlayerWithoutOS player){
		this.leavingPlayer=player;
	}
	
	public void setNewType(HistoryType type){
		this.type=type;
	}
	
	public void setWinner(PlayerWithoutOS winner){
		this.winner=winner;
	}
	
	public PlayerWithoutOS getWinner(){
		return this.winner;
	}

}