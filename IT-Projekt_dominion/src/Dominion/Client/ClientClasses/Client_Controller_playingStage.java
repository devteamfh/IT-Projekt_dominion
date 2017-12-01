package Dominion.Client.ClientClasses;

import java.io.IOException;

import Dominion.Client.abstractClasses.Controller;
import Dominion.appClasses.CancelGame;
import Dominion.appClasses.GameObject;
import Dominion.appClasses.GameParty;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * Copyright 2015, FHNW, Prof. Dr. Brad Richards. All rights reserved. This code
 * is licensed under the terms of the BSD 3-clause license (see the file
 * license.txt).
 * 
 * @author Brad Richards (MVC), Joel Henz (events)
 */
public class Client_Controller_playingStage extends Controller<Client_Model, Client_View_playingStage> {
	private ServiceLocatorClient sl;
	private Client_View_playingStage view_playingStage;
       
    /**
     * @author Joel Henz
     * */
    public Client_Controller_playingStage(Client_Model model, Client_View_playingStage view, GameParty gameParty) {
        super(model, view);
        this.view_playingStage = view;
        sl = ServiceLocatorClient.getServiceLocator(); 
        
        view.endGameHost.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	CancelGame cancel = new CancelGame(gameParty);
            	try {
					model.out.writeObject(cancel);
					model.out.flush();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            	
            	
            }
        });
    }

	
}