package Dominion.Client.ClientClasses;

import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Logger;

import Dominion.appClasses.GameParty;
import Dominion.appClasses.JoinGameParty;
import Dominion.appClasses.StartInformation;
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
 
    private GameParty currentGameParty;
    
    Label numberOfActionsAndBuys = new Label();
    
    private boolean isHost=false;
    
    /**
     * @author kab
     */
    private TableView <StartInformation> tbl_playerStats = new TableView<StartInformation>();
    /**
     * @author kab
     */
    private ArrayList<StartInformation> al_Statistics = new ArrayList <StartInformation>();
    
    //if the host ends his game before the GameParty is full, the game will end for the host and all other clients and will disappear on the ListView "Spielübersicht" in the lobby. 
  	//There will be no score for this GameParty. Once the GameParty is full, the GameParty will disappear on the ListView. While playing the game, each client is able to leave the GameParty. His score
  	//will be evaluated as a loss.
    private Button endGameHost = new Button ("Spiel beenden (Host)");

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
		//this.obsList.remove(game);
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
				this.obsList.set(i, gamePartyToJoin);
				if(this.obsList.get(i).isFull()){
					this.obsList.remove(i);
					this.endGameHost.setDisable(true);
					break;
				}
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
	
	public Button getButtonEndGameHost(){
		return this.endGameHost;
	}




}