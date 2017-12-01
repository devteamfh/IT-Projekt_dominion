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
import javafx.scene.control.ListView;
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
 * @author Brad Richards, resources/methods by Joel Henz
 * 
 */
public class ServiceLocatorClient {
    private static ServiceLocatorClient serviceLocator; // singleton

    // Application-global constants
    final private String APP_NAME = "Dominion";

    // Resources
    private Logger logger = Logger.getLogger("");
    
    private ToggleGroup endOfGameGroup;
    private ToggleGroup numberOfPlayersGroup;
    private TextField inputNumberOfRounds;
    
    private TextArea ta_lobby = new TextArea();
    
    private ObservableList<GameParty> obsList = FXCollections.observableArrayList();
    
    private ListView <GameParty> gameList;
    
    private Client_View_playingStage view_playingStage;
    private Client_View_createGame view_createGame;
    

    private ObservableList<StartInformation> ol_StartInformation = FXCollections.observableArrayList();
    private ListView <StartInformation> lv_StartInformation;
    
    private GameParty currentGameParty;
    
    /**
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
    
    public TextArea getTextAreaLobby(){
    	return this.ta_lobby;
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
		this.obsList.remove(game);
		
		Iterator <GameParty> iter = this.obsList.iterator();
		while(iter.hasNext()){
			System.out.println(iter.next());
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
	
	public void setListView_StartInformation(){
		 this.lv_StartInformation = new ListView<>(ol_StartInformation);
	}
	
	public ListView <StartInformation> getListView_StartInformation(){
		return this.lv_StartInformation;
	}
	
	public void setCurrentGameParty(GameParty party){
		this.currentGameParty=party;
	}
	
	public GameParty getCurrentGameParty(){
		return this.currentGameParty;
	}

}