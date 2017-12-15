package Dominion.Client.ClientClasses;

import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Logger;

import Dominion.Client.ClientClasses.gameplay.Croupier;
import Dominion.Client.ClientClasses.gameplay.cards.Cards;
import Dominion.appClasses.GameParty;
import Dominion.appClasses.JoinGameParty;
import Dominion.appClasses.PlayerWithOS;
import Dominion.appClasses.PlayerWithoutOS;
import Dominion.appClasses.StartInformation;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;

/**
 * Copyright 2015, FHNW, Prof. Dr. Brad Richards. All rights reserved. This code
 * is licensed under the terms of the BSD 3-clause license (see the file
 * license.txt).
 * 
 * The singleton instance of this class provide central storage for resources
 * used by the program. It also defines application-global constants, such as
 * the application name.
 * 
 * @author Brad Richards, resources/methods by Joel Henz (if not mentioned other author)
 * 
 */
public class ServiceLocatorClient {
    private static ServiceLocatorClient serviceLocator; // singleton

    // Application-global constants
    final private String APP_NAME = "Dominion";

    private Logger logger = Logger.getLogger("");
    
    private PlayerWithoutOS player_noOS;
    private PlayerWithOS player_OS;
    //we need this to save because we need to know which player is currently playing when we leave the game
    private PlayerWithoutOS currentPlayerOfPlayingStage;
    
    private ToggleGroup endOfGameGroup;
    private ToggleGroup numberOfPlayersGroup;
    private TextField inputNumberOfRounds;
    
    private TextArea ta_ChatLobby = new TextArea();
    private TextArea ta_ChatPlayingStage = new TextArea();
    private TextArea ta_gameHistory = new TextArea();
    
    private ObservableList<GameParty> obsList = FXCollections.observableArrayList();
    private ListView <GameParty> gameList;
    
    private Client_View_playingStage view_playingStage;
    private Client_View_createGame view_createGame;
    private Client_View_lobby view_lobby;
 
    private GameParty currentGameParty;
    
    private Label numberOfActionsAndBuys = new Label();
    private Button provisorischCard1;
    private Button provisorischCard2;
    private Button provisorischCard3;	
    private Button playAction; //nur provisorisch
    private Button playBuy;    // nur provisorisch
    private Button endAction;   //bleiben  
    private Button endBuy;      //bleiben
    
    private boolean isHost=false;
    
    private ArrayList<StartInformation> al_Statistics = new ArrayList <StartInformation>();
    private TableView <StartInformation> tbl_playerStats = new TableView<StartInformation>();

    private Label lbl_errMsgView = new Label("");   
    private Label lbl_popUpMessage = new Label("");
   
    //if the host ends his game before the GameParty is full, the game will end for the host and all other clients and will disappear on the ListView "Spielübersicht" in the lobby. 
  	//There will be no score for this GameParty. Once the GameParty is full, the GameParty will disappear on the ListView. While playing the game, each client is able to leave the GameParty. His score
  	//will be evaluated as a loss.
    //private Button endGameHost = new Button ("Spiel beenden (Host)");
    
    //button for leaving the game. If a party starts, each player gets a defeat if he clicks this button
    //while waiting for full game, this button is deactivated for the host because he can end the game via "Spiel beenden (Host)" --> see button implemented above. For all other players while waiting
    //for full game: via button endGamePlayer they can leave the game without getting a defeat
   // private Button endGamePlayer = new Button ("Spiel verlassen");
    
    private Croupier croupier;

    /**
     * @author Brad Richards
     * Factory method for returning the singleton
     * @param mainClass The main class of this program
     * @return The singleton resource locator
     */
    public static ServiceLocatorClient getServiceLocator() {
        if (serviceLocator == null)
            serviceLocator = new ServiceLocatorClient();
        return serviceLocator;
    }

    /**
     * @author Brad Richards
     * Private constructor, because this class is a singleton
     * @param appName Name of the main class of this program
     */
    private ServiceLocatorClient() {
        // Currently nothing to do here. We must define this constructor anyway,
        // because the default constructor is public
    }

    public String getAPP_NAME() {
        return APP_NAME;
    }
    
    public Logger getLogger() {
        return logger;
    }

    public void setLogger(Logger logger) {
        this.logger = logger;
    }
    
    //player without ObjectOutputStream
    public void setPlayer_noOS(PlayerWithoutOS player){
    	this.player_noOS=player;
    }
    
    public PlayerWithoutOS getPlayer_noOS(){
    	return this.player_noOS;
    }
    
    public void setCurrentPlayer_noOS_ofPlayingStage(PlayerWithoutOS player){
    	this.currentPlayerOfPlayingStage=player;
    }
    
    public PlayerWithoutOS getCurrentPlayer_noOS_ofPlayingStage(){
    	return this.currentPlayerOfPlayingStage;
    }
    
    //player WITH ObjectOutpuStream
    public void setPlayer_OS(PlayerWithOS player){
    	this.player_OS=player;
    }
    
    public PlayerWithOS getPlayer_OS(){
    	return this.player_OS;
    }
    
    public ToggleGroup getToggleForEndOfGame(){
    	return this.endOfGameGroup;
    }
    
    public void setToggleForEndOfGame(){
    	this.endOfGameGroup = new ToggleGroup();
    }
    
    public ToggleGroup getToggleForNumberOfPlayers(){
    	return this.numberOfPlayersGroup;
    }
    
    public void setToggleForNumberOfPlayers(){
    	this.numberOfPlayersGroup = new ToggleGroup();
    }
    
    public TextField getTextFieldForRounds(){
    	return this.inputNumberOfRounds;
    }
    
    public void setTextFieldForRounds(){
    	this.inputNumberOfRounds = new TextField();
    }
    
    public TextArea getTextAreaChatLobby(){
    	return this.ta_ChatLobby;
    }
    
    public TextArea getTextAreaChatPlayingStage(){
    	return this.ta_ChatPlayingStage;
    }
    
    public TextArea getTextAreaGameHistory(){
    	return this.ta_gameHistory;
    }

	public ListView <GameParty> getListView() {
		// TODO Auto-generated method stub
		return this.gameList;
	}
	
	public void setListView(){		 
		 this.gameList = new ListView<>(obsList);
	}
	
	public void addNewGame(GameParty newGame){
		this.obsList.add(newGame);		
	}
	
	public void removeGame(GameParty game){	
		long id = game.getID();
		for (int i=0; i<this.obsList.size();i++){
			if(id == this.obsList.get(i).getID()){
				this.obsList.remove(i);				
				break;
			}
		}
		
	}	
	
	public void updateGameParty(JoinGameParty join){
		GameParty gamePartyToJoin = join.getSelectedGameParty();
		long id = gamePartyToJoin.getID();
		
		for (int i=0; i<this.obsList.size();i++){
			if(id == this.obsList.get(i).getID()){
				//replace the old GameParty with the updated GameParty
				this.obsList.set(i, gamePartyToJoin);
				if(this.obsList.get(i).isFull()){					
					GameParty toPrepare = this.obsList.get(i);
					this.obsList.remove(i);
					//the game is full so it can begin
					prepareGame(toPrepare);					
				}
				
				//corresponding obs List element found (1. for-loop)
				break;
			}
		}		
	} 
	
	//prepare the playing stage of the host so he can start the game
	public void prepareGame(GameParty party){		
		if(party.getHost().getUsername().equals(this.player_noOS.getUsername())){
			//this.playAction.setDisable(false);
			this.endAction.setDisable(false);
			this.croupier.setActionMode(true);
			this.croupier.setActions(1);
			this.croupier.setBuys(1);
			this.croupier.setBuyPower(0);
			//this.endGameHost.setDisable(true);
			//activate the button for leaving games also for the host
			//this.endGamePlayer.setDisable(false);
			
			//change the Label "warten bis Spiel voll ist..." for the players of this GameParty
			
			this.numberOfActionsAndBuys.setText("Du bist an der Reihe: "+croupier.getActions()+" Aktionen, "+croupier.getBuys()+" Käufe, "+croupier.getBuyPower()+" Geld");
			
			this.ta_gameHistory.appendText("Spiel beginnt\n");
			this.ta_gameHistory.appendText(party.getHost().getUsername()+" ist an der Reihe\n");
			this.ta_gameHistory.selectPositionCaret(this.ta_gameHistory.getText().length());
		}else{
			this.numberOfActionsAndBuys.setText(party.getHost().getUsername()+" ist an der Reihe: 1 Aktionen, 1 Käufe, 0 Geld");
			
			this.ta_gameHistory.appendText("Spiel beginnt\n");
			this.ta_gameHistory.appendText(party.getHost().getUsername()+" ist an der Reihe\n");
			this.ta_gameHistory.selectPositionCaret(this.ta_gameHistory.getText().length());
		}

	}
	
	public void updateGamePartyAfterLeave(GameParty party){
		long id = party.getID();
		
		for (int i=0; i<this.obsList.size();i++){
			if(id == this.obsList.get(i).getID()){
				//replace the old GameParty with the updated GameParty
				this.obsList.set(i, party);
				
				break;
			}
		}
	}
	
	public void setView_playingStage (Client_View_playingStage view){
		this.view_playingStage=view;
	}
	
	public void setView_createGame (Client_View_createGame view){
		this.view_createGame=view;
	}
	
	public Client_View_playingStage getPlayingStage(){
		return this.view_playingStage;
	}
	
	public Client_View_createGame getCreateGameView(){
		return this.view_createGame;
	}
	
	/**
	* @author kab: List information for Player Statistics Table
	*/
	public TableView <StartInformation> getTbl_playerStats() {
		return tbl_playerStats;
	}

	/**
	* @author kab: List information for Player Statistics Table
	*/
	public void setTbl_playerStats(TableView <StartInformation> tbl_playerStats) {
		this.tbl_playerStats = tbl_playerStats;
	}
	
	/**
	* @author kab: List information for Player Statistics Table
	*/
	public ArrayList<StartInformation> getAl_Statistics() {
		return al_Statistics;
	}

	/**
	* @author kab: List information for Player Statistics Table
	*/
	public void setAl_Statistics(ArrayList<StartInformation> al_Statistics) {
		this.al_Statistics = al_Statistics;
	}

	/**
	* @author kab: List information for Player Statistics Table
	*/
	public void add_AL_Statistics(ArrayList<StartInformation> statistics){
		for(int i = 0;i<statistics.size();i++){
			this.al_Statistics.add(statistics.get(i));
		}
		
	}

	public void setCurrentGameParty(GameParty party){
		this.currentGameParty=party;
	}
	
	public void removeCurrentGameParty(){
		this.currentGameParty=null;
	}
	
	public GameParty getCurrentGameParty(){
		return this.currentGameParty;
	}
	
	public Label getLabelNumberOfActionsAndBuys(){
		return this.numberOfActionsAndBuys;
	}
	
	public boolean getIsHost(){
		return this.isHost;
	}
	
	public void setIsHost(boolean isHost){
		this.isHost=isHost;
	}
	
//	public Button getButtonEndGameHost(){
//	return this.endGameHost;
//}

//public Button getButtonLeaveGamePlayer(){
//	return this.endGamePlayer;
//}
	
	//getter and setter for some playing stage buttons
	public Button getButtonPlayActions(){
		return this.playAction;
	}
	
	public void setButtonPlayActions(String text){
		this.playAction = new Button (text);
	}
	
	public Button getButtonPlayBuy(){
		return this.playBuy;
	}
	
	public void setButtonPlayBuy(String text){
		this.playBuy = new Button (text);
	}
	
	public Button getButtonEndActions(){
		return this.endAction;
	}
	
	public void setButtonEndActions(String text){
		this.endAction = new Button (text);
	}
	
	public Button getButtonEndBuys(){
		return this.endBuy;
	}
	
	public void setButtonEndBuys(String text){
		this.endBuy = new Button (text);
	}
	

	public Label getLbl_errMsgView() {
		return lbl_errMsgView;
	}

	public void setLbl_errMsgView(String str) {
		this.lbl_errMsgView.setText(str);
	}

	public Label getLbl_popUpMessage() {
		return lbl_popUpMessage;
	}

	public void setLbl_popUpMessage(Label lbl_popUpMessage) {
		this.lbl_popUpMessage = lbl_popUpMessage;
	}

	public Client_View_lobby getView_lobby() {
		return view_lobby;
	}

	public void setView_lobby(Client_View_lobby view_lobby) {
		this.view_lobby = view_lobby;
	}
	
	public void setCroupier(Croupier croupier){
		this.croupier= croupier;
	}
	
	public Croupier getCroupier(){
		return this.croupier;
	}
	
	public void clearCurrentGameParty(){
		this.currentGameParty = null;
		this.view_playingStage.stop();
		this.view_playingStage.root.getChildren().clear();
		//set croupier singleton null
		this.croupier.clear();
		this.ta_ChatPlayingStage.clear();
		this.ta_gameHistory.clear();
	}
	



}