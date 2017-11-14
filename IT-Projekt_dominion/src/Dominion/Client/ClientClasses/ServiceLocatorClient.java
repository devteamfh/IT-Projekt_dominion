package Dominion.Client.ClientClasses;

import java.util.ArrayList;
import java.util.logging.Logger;

import Dominion.appClasses.GameParty;
import Dominion.appClasses.Player;
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
 * @author Brad Richards, resources by Joel Henz
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

    private String selectedMode = new String();
    private int numberOfPlayers;
    
    private TextArea ta_lobby = new TextArea();
    
    private ObservableList<GameParty> obsList = FXCollections.observableArrayList();
    private ListView <GameParty> gameList;

    
    private ObservableList<Player> obsList_players = FXCollections.observableArrayList();
    private ListView <Player> playerList;
    
    //kab
    private ObservableList<StartInformation> obsList_StartInformation = FXCollections.observableArrayList();
    private ListView <StartInformation> LV_StartInformation;
    
    

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
    
    public void setSelectedMode(String selectedMode){
    	this.selectedMode = selectedMode;
    }
    
    public String getSelectedMode (){
    	return this.selectedMode;
    }
    
    public void setNumberOfPlayer(int number){
    	this.numberOfPlayers=number;
    }
    
    public int getNumberOfPlayers(){
    	return this.numberOfPlayers;
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
	
	public void clearListOfOpenGames(){
		this.obsList.clear();
	}
	
	//kab ListView Start Information
	public ListView <StartInformation> getListView_StartInformation() {
		// TODO Auto-generated method stub
		return this.LV_StartInformation;
	}
	//kab
	public void setListView_StartInformation(){		 
		 this.LV_StartInformation = new ListView<>(obsList_StartInformation);
	}
	//kab
	public void addNewGame(StartInformation startInfo){
		this.obsList_StartInformation.add(startInfo);
		
	}
	//kab
	public void clearList_obsList_StartInformation(){
		this.obsList_StartInformation.clear();
	}

  
}