package Dominion.appClasses;

import Dominion.Client.ClientClasses.gameplay.cards.GameCard;

/**
 * @author Joel Henz:
 * GameHistory is used for communication between client and server while playing on playing stage. An instance of this class can save different information which the server and the opponents have to know
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
	private String card;
	private int buyPower;
	
	public enum HistoryType {
		 PlayAction, PlayBuy, EndAction,
		 EndBuy, LeaveGame, UpdateLobbyAfterLeave,
		 EndGame, PlayMoneyCard, PlayActionCard
		 };
	
	private static long nextMessageID() {		
		return messageID++;
	}
	
	//we use this constructor for events like: player leaves a game / host ends the game / a player ends his buy or action phase by clicking on the buttons "Kaufphase beenden" oder "Aktionsphase beenden" / etc
	public GameHistory(String text, GameParty party,PlayerWithoutOS currentPlayer, HistoryType type){
		super(GameObject.ObjectType.GameHistory);
		this.id=-1;
		this.text=text;
		this.party=party;
		this.currentPlayer=currentPlayer;
		this.type=type;
	}
	
	//we use this constructor when we play a money card
	public GameHistory(GameParty party,PlayerWithoutOS currentPlayer,String card,int buyPower, HistoryType type){
		super(GameObject.ObjectType.GameHistory);
		this.id=-1;
		this.party=party;
		this.currentPlayer=currentPlayer;
		this.card=card;
		this.buyPower=buyPower;
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
	
	public String getPlayedGameCard(){
		return this.card;
	}
	
	public int getBuyPower(){
		return this.buyPower;
	}

}