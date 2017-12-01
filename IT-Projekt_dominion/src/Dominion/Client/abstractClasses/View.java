package Dominion.Client.abstractClasses;

import Dominion.appClasses.GameParty;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * MVC Pattern:
 * Copyright 2015, FHNW, Prof. Dr. Brad Richards. All rights reserved. This code
 * is licensed under the terms of the BSD 3-clause license (see the file
 * license.txt).
 * 
 * @author Brad Richards
 */

public abstract class View<M extends Model> {
    protected Stage stage;
    protected Scene scene;
    protected M model;
    protected String name;
    protected GameParty party;
    
    /**
     * Set any options for the stage in the subclass constructor
     * 
     * @param stage
     * @param model
     */
    
    protected View(Stage stage, M model) {
        this.stage = stage;
        this.model = model;
        
        scene = create_GUI(); // Create all controls within "root"
        stage.setScene(scene);
    }
    
    /**
     * @author Joel Henz: 
     * only needed for Client_View_playingStage
     */
    protected View(Stage stage, M model, GameParty party) {
        this.stage = stage;
        this.model = model;
        this.party=party;
        
        scene = create_GUI(); // Create all controls within "root"
        stage.setScene(scene);
    }
    
    

    protected abstract Scene create_GUI();

    /**
     * Display the view
     */
    public void start() {
        stage.show();
    }
    
    /**
     * Hide the view
     */
    public void stop() {
        stage.hide();
    }
    
    /**
     * Getter for the stage, so that the controller can access window events
     */
    public Stage getStage() {
        return stage;
    }
}
