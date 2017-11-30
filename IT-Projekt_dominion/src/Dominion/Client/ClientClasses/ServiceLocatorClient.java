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

    private String selectedMode = new String();
    private int numberOfPlayers;
    
    private TextArea ta_lobby = new TextArea();
    
    private ObservableList<GameParty> obsList = FXCollections.observableArrayList();
    private ListView <GameParty> gameList;
    
    private Client_View_playingStage view_playingStage;
    
    private TableView <StartInformation> tbl_playerStats = new TableView<StartInformation>();
    private ArrayList<StartInformation> al_Statistics = new ArrayList <StartInformation>();
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
	
	public Client_View_playingStage getPlayingStage(){
		return this.view_playingStage;
	}
	
	
	/**
	 * @author kab: List information for Player Statistics Table
	 * 
	 * 
	 * 
	 */
		public TableView <StartInformation> getTbl_playerStats() {
		return tbl_playerStats;
	}

	public void setTbl_playerStats(TableView <StartInformation> tbl_playerStats) {
		this.tbl_playerStats = tbl_playerStats;
	}


	
	
	public ArrayList<StartInformation> getAl_Statistics() {
		return al_Statistics;
	}

	public void setAl_Statistics(ArrayList<StartInformation> al_Statistics) {
		this.al_Statistics = al_Statistics;
	}
	
	public void add_AL_Statistics(ArrayList<StartInformation> statistics){
		for(int i = 0;i<statistics.size();i++){
			this.al_Statistics.add(statistics.get(i));
		}
		
	}
	
	

	
	
	

}