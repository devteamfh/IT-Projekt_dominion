package Dominion.Client.ClientClasses;

import Dominion.Client.abstractClasses.Controller;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

/**
 * Copyright 2015, FHNW, Prof. Dr. Brad Richards. All rights reserved. This code
 * is licensed under the terms of the BSD 3-clause license (see the file
 * license.txt).
 * 
 * @author Brad Richards (MVC), Joel Henz (events)
 */
public class Client_Controller_hostEndedGame extends Controller<Client_Model, Client_View_hostEndedGame> {
       
    /**
     * @author Joel Henz
     * */
    public Client_Controller_hostEndedGame(Client_Model model, Client_View_hostEndedGame view) {
        super(model, view);

        view.ok.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	view.stop();   	
            }
        });
        
    }


	
}