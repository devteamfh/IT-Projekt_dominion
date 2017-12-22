package Dominion.appClasses;

import java.util.ArrayList;

import Dominion.Client.ClientClasses.gameplay.Croupier;
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
	
	private String textForTextArea;
	private String textForLabel;
	private GameParty party;
	private PlayerWithoutOS playerForGUIActivation;
	private PlayerWithoutOS currentPlayer;
	private PlayerWithoutOS leavingPlayer;
	private ArrayList<PlayerWithoutOS> winner;
	private HistoryType type;
	private String card_EN;
	private String card_DE;
	private PlayerWithoutOS playerForWitchMode;
	
	public enum HistoryType {
		 EndAction, EndBuy, LeaveGame, UpdateLobbyAfterLeave,
		 EndGame, PlayCard, BuyPointCard, BuyNoPointCard,
		 Trash, MineModeEnd, Discard, RebuildingModeEnd,
		 WorkshopModeEnd, Reaction, AttackEnd, EndGameModeProvince
		 };
	
	private static long nextMessageID() {		
		return messageID++;
	}
	
	//we use this constructor for events like: player leaves a game / host ends the game
	public GameHistory(String textForTextArea, GameParty party,PlayerWithoutOS currentPlayer, HistoryType type){
		super(GameObject.ObjectType.GameHistory);
		this.id=-1;
		this.textForTextArea=textForTextArea;
		this.party=party;
		this.currentPlayer=currentPlayer;
		this.type=type;
	}
	
	
	//we use this constructor while playing (playing cards, ending action phase, ending buy phase, buying cards etc)
	public GameHistory(String textForTextArea, String textForLabel, GameParty party,PlayerWithoutOS currentPlayer,String card_EN,String card_DE, HistoryType type){
		super(GameObject.ObjectType.GameHistory);
		this.id=-1;
		this.textForTextArea=textForTextArea;
		this.textForLabel=textForLabel;
		this.party=party;
		this.currentPlayer=currentPlayer;
		this.card_EN=card_EN;
		this.card_DE=card_DE;
		this.type=type;
	}
	
	//constructor only for end the game in Mode "Provinzkarte"
		public GameHistory(String textPopUp,GameParty party,PlayerWithoutOS currentPlayer,String card_EN, HistoryType type){
			super(GameObject.ObjectType.GameHistory);
			this.id=-1;
			this.textForTextArea = textPopUp;
			this.party=party;
			this.currentPlayer=currentPlayer;
			this.card_EN=card_EN;
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
	
	public void updateGameParty(GameParty party){
		this.party=party;
	}
	
	public String getTextForTextArea(){
		return this.textForTextArea;
	}
	
	public String getTextForLabel(){
		return this.textForLabel;
	}
	
	public void updateTextForTextArea(String text){
		this.textForTextArea = text;
	}
	
	public void clearText(){
		this.textForTextArea = "";
	}
	
	public String getGameCard_EN(){
		return this.card_EN;
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
	
	public void setWinnerList(ArrayList<PlayerWithoutOS> winner){
		this.winner=winner;
	}
	
	public ArrayList<PlayerWithoutOS> getWinner(){
		return this.winner;
	}
	
	//we have to save here our own player when we have to pick up a curse card so the server knows where to take away a point
	public void setPlayerForWitchMode(PlayerWithoutOS player){
		 this.playerForWitchMode = player;
	}
	
	public PlayerWithoutOS getPlayerForWitchMode(){
		 return this.playerForWitchMode;
	}

}