package Dominion.Client.ClientClasses;

import Dominion.Client.abstractClasses.Controller;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Copyright 2015, FHNW, Prof. Dr. Brad Richards. All rights reserved. This code
 * is licensed under the terms of the BSD 3-clause license (see the file
 * license.txt).
 * 
 * @author Brad Richards (MVC), Joel Henz (events), kab
 */
public class Client_Controller_popUp extends Controller<Client_Model, Client_View_popUp> {
      ServiceLocatorClient sl;
    /**
     * @author Joel Henz
     * */
    public Client_Controller_popUp(Client_Model model, Client_View_popUp view) {
        super(model, view);
        sl = ServiceLocatorClient.getServiceLocator();

        view.ok.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	view.stop();

            }
        
        });
  
    }
	
}