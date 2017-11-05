package Dominion.Server.ServerClasses;

import java.util.ArrayList;
import java.util.logging.Logger;

import Dominion.appClasses.GameParty;
import Dominion.appClasses.Player;
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
    private ArrayList <GameParty> listOfOpenGames = new ArrayList <GameParty>();
    private ArrayList<Player> connectedPlayers = new ArrayList<Player>();
    

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
    
    public void addOpenGame(GameParty party){
    	this.listOfOpenGames.add(party);
    }
    
    public ArrayList <GameParty> getGameList(){
    	return this.listOfOpenGames;    	
    }
    
    public void addConnectedPlayer(Player player){
    	this.connectedPlayers.add(player);
    }
    
    public ArrayList <Player> getConnectedPlayers(){
    	return this.connectedPlayers;
    }
    
}