package Dominion;

import java.util.ArrayList;
import java.util.logging.Logger;

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
 * @author Brad Richards, resources by Joel Henz
 * 
 */
public class ServiceLocator {
    private static ServiceLocator serviceLocator; // singleton

    // Application-global constants
    final private String APP_NAME = "Dominion";

    // Resources
    private Logger logger = Logger.getLogger("");
    
    private int NumOfLogged_players = 0;

    private TextArea ta;
    
    private int playedGames = 0;

    /**
     * Factory method for returning the singleton
     * @param mainClass The main class of this program
     * @return The singleton resource locator
     */
    public static ServiceLocator getServiceLocator() {
        if (serviceLocator == null)
            serviceLocator = new ServiceLocator();
        return serviceLocator;
    }

    /**
     * Private constructor, because this class is a singleton
     * @param appName Name of the main class of this program
     */
    private ServiceLocator() {
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
     
    public int getNumOfLoggedPlayer(){
    	return this.NumOfLogged_players;
    }
    
    public void increaseLoggedPlayer(){
    	this.NumOfLogged_players++;
    }
    
    public TextArea getTextArea(){
    	return this.ta;
    }
    
    public void setTextArea(){
    	this.ta = new TextArea();
    }
    
    public void increasePlayedGamesCounter(){
    	this.playedGames++;
    }
    
    public int getPlayedGamesCounter(){
    	return this.playedGames;
    }
  
}
