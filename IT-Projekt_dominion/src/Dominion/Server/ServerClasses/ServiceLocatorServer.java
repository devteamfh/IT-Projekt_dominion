package Dominion.Server.ServerClasses;

import java.util.ArrayList;
import java.util.logging.Logger;
import Dominion.Server.ServerClasses.Database;
import Dominion.appClasses.GameParty;
import Dominion.appClasses.PlayerWithOS;
import Dominion.appClasses.StartInformation;
import javafx.scene.control.TextArea;


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
public class ServiceLocatorServer {
    private static ServiceLocatorServer serviceLocator; // singleton

    // Application-global constants
    final private String APP_NAME = "Dominion";

    // Resources
    private Logger logger = Logger.getLogger("");
    private ArrayList <GamePartyOnServer> listOfOpenGamePartiesOnServer = new ArrayList <GamePartyOnServer>();
    private ArrayList<PlayerWithOS> connectedPlayers = new ArrayList<PlayerWithOS>();
    private Database database = new Database();
    private ArrayList<StartInformation> al_startInfoStatistics = new ArrayList <StartInformation>();
    

    /**
     * Factory method for returning the singleton
     * @param mainClass The main class of this program
     * @return The singleton resource locator
     */
    public static ServiceLocatorServer getServiceLocator() {
        if (serviceLocator == null)
            serviceLocator = new ServiceLocatorServer();
        return serviceLocator;
    }

    /**
     * Private constructor, because this class is a singleton
     * @param appName Name of the main class of this program
     */
    private ServiceLocatorServer() {
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
    
    public void addNewGame(GamePartyOnServer party){
    	this.listOfOpenGamePartiesOnServer.add(party);
    }
    
    public void removeFullGame(GamePartyOnServer party){
    	this.listOfOpenGamePartiesOnServer.remove(party);
    }
    
    public ArrayList <GamePartyOnServer> getGameListFromServer(){
    	return this.listOfOpenGamePartiesOnServer;    	
    }
    
    public void addConnectedPlayer(PlayerWithOS player){
    	this.connectedPlayers.add(player);
    }
    
    public ArrayList <PlayerWithOS> getConnectedPlayers(){
    	return this.connectedPlayers;
    }
    
    /**
     * @author kab: statistic Information
     * mit diesen weiterfahren
     */
   
    
   // public void removeFullGame(GamePartyOnServer party){
   // 	this.al_startInfoStatistics.remove(party);
   // }
    
    public ArrayList <StartInformation> get_al_AllStartInfoStatisitcsOnServer(){
    	return this.al_startInfoStatistics;
    }
    public void addNewStartInfoStatistics(StartInformation startInfo){
    	this.al_startInfoStatistics.add(startInfo);
    }

    public void removeStartInfoStatistics(String username){
    	
    	for (int i = 0; i < al_startInfoStatistics.size();i++){
    		if (al_startInfoStatistics.get(i).getUsername().equals(username)){
    			al_startInfoStatistics.remove(i);
    			System.out.println("Statistik mit nachfolgendem index in liste auf server gel�scht: "+i);
    		} 	
    		System.out.println("neue al_startinfostatarraylist gr�sse: "+al_startInfoStatistics.size());
    	}	
    }
    
    /**
     * @author: kab
     * Methoden Datenbank mit Statistik
     */
    
    //Player hinzuf�gen
    public void db_addPlayer(String user, String pw, int att2, int att3, int att4, int att5, String att6, String att7, String att8, String att9){
    	this.database.addPlayer(user, pw, att2, att3, att4, att5, att6, att7, att8, att9);
    }
    
    //Statistik updaten
    public void db_updateStatistics(){
    
    }
    
    
    
    
}